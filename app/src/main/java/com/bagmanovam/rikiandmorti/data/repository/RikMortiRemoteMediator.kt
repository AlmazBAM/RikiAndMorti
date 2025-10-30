package com.bagmanovam.rikiandmorti.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.bagmanovam.rikiandmorti.data.db.RikMoritDao
import com.bagmanovam.rikiandmorti.data.db.RikMortiEntity
import com.bagmanovam.rikiandmorti.data.internet.RikMortiApi
import com.bagmanovam.rikiandmorti.data.mapper.dtoToEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
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
                val lastItem = state.lastItemOrNull() ?: return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                lastItem.id / state.config.pageSize + 1
            }
        }
        return try {
            val response = api.getRikMortiHeroes(page)
            Log.e("REMOTE_MEDIATOR", "Response: ${response.code()} body=${response.body()}")
            if (!response.isSuccessful) return MediatorResult.Error(Exception("Network error"))
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
}