@file:OptIn(ExperimentalPagingApi::class)

package com.bagmanovam.rikiandmorti.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.bagmanovam.rikiandmorti.data.db.RikMoritDao
import com.bagmanovam.rikiandmorti.data.db.RikMortiEntity
import com.bagmanovam.rikiandmorti.data.internet.RikMortiApi
import com.bagmanovam.rikiandmorti.data.model.RikMortiHeroesDto
import com.bagmanovam.rikiandmorti.data.mapper.dtoToEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import retrofit2.Response
import kotlin.math.min

class RikMortiRemoteMediator(
    private val api: RikMortiApi,
    private val dao: RikMoritDao,
) : RemoteMediator<Int, RikMortiEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RikMortiEntity>,
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val maxLoadedId = withContext(Dispatchers.IO) { dao.getMaxHeroId() } ?: return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                (maxLoadedId / state.config.pageSize) + 1
            }
        }
        return try {
            val response = requestPageWithRetry(page)
            Log.d("REMOTE_MEDIATOR", "Response code=${response.code()} page=$page")
            if (!response.isSuccessful) {
                if (response.code() == 404) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                return MediatorResult.Error(Exception("Network error: ${response.code()}"))
            }
            val body = response.body() ?: return MediatorResult.Success(true)

            val entities = body.results.map { dto ->
                dto.dtoToEntity()
            }

            withContext(Dispatchers.IO) {
                dao.addRikMortiHeroes(entities)
            }
            MediatorResult.Success(endOfPaginationReached = body.info.next == null)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun requestPageWithRetry(page: Int): Response<RikMortiHeroesDto> {
        var response = requestPage(page)
        var attempt = 0

        while (response.code() == TOO_MANY_REQUESTS && attempt < MAX_RETRY_ATTEMPTS) {
            attempt++
            val retryAfterSec = response.headers()[RETRY_AFTER_HEADER]?.toLongOrNull()
            val delayMillis = when {
                retryAfterSec != null -> min(retryAfterSec, MAX_RETRY_AFTER_SECONDS) * 1000L
                else -> RETRY_DELAY_MS * attempt
            }
            Log.w("REMOTE_MEDIATOR", "Rate limited on page=$page, retry in ${delayMillis}ms")
            delay(delayMillis)
            response = requestPage(page)
        }

        return response
    }

    private suspend fun requestPage(page: Int): Response<RikMortiHeroesDto> {
        requestMutex.withLock {
            val now = System.currentTimeMillis()
            val elapsedSinceLastRequest = now - lastRequestTimeMillis
            if (elapsedSinceLastRequest < MIN_REQUEST_INTERVAL_MS) {
                delay(MIN_REQUEST_INTERVAL_MS - elapsedSinceLastRequest)
            }
            lastRequestTimeMillis = System.currentTimeMillis()
        }
        return api.getRikMortiHeroes(page)
    }

    private companion object {
        const val TOO_MANY_REQUESTS = 429
        const val MAX_RETRY_ATTEMPTS = 4
        const val RETRY_DELAY_MS = 800L
        const val MIN_REQUEST_INTERVAL_MS = 1200L
        const val MAX_RETRY_AFTER_SECONDS = 15L
        const val RETRY_AFTER_HEADER = "retry-after"
        val requestMutex = Mutex()
        var lastRequestTimeMillis = 0L
    }
}