package com.bagmanovam.rikiandmorti.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.bagmanovam.rikiandmorti.core.domain.NetworkError
import com.bagmanovam.rikiandmorti.core.domain.Result
import com.bagmanovam.rikiandmorti.data.db.RikMoritDao
import com.bagmanovam.rikiandmorti.data.internet.RikMortiApi
import com.bagmanovam.rikiandmorti.data.mapper.entityToDomain
import com.bagmanovam.rikiandmorti.data.mapper.toDomain
import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero
import com.bagmanovam.rikiandmorti.domain.repository.SearchRikMortiHeroesRepository
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.nio.channels.UnresolvedAddressException
import kotlin.coroutines.coroutineContext

@OptIn(ExperimentalPagingApi::class)
class SearchRikMortiHeroesRepositoryImpl(
    private val api: RikMortiApi,
    private val dao: RikMoritDao
) : SearchRikMortiHeroesRepository {
    override fun requestRikMortiHeroes(): Flow<PagingData<RikMortiHero>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 3,
                enablePlaceholders = false
            ),
            remoteMediator = RikMortiRemoteMediator(api, dao),
            pagingSourceFactory = { dao.getPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { it.entityToDomain() }
        }
    }

    override suspend fun requestRikMortiHero(itemId: Int): Result<RikMortiHero, NetworkError> {
        return try {
            val response = api.getRikMortiHero(itemId)
            when (response.code()) {
                in 200..299 -> {
                    try {
                        val bd = response.body()
                        if (bd != null)
                            Result.Success(bd.toDomain())
                        else
                            Result.Error(NetworkError.SERVER_ERROR)
                    } catch (_: Exception) {
                        Result.Error(NetworkError.SERVER_ERROR)
                    }
                }

                408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
                429 -> Result.Error(NetworkError.TOO_MANY_REQUESTS)
                in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
                else -> {
                    Result.Error(NetworkError.UNKNOWN)
                }
            }
        } catch (_: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (_: UnknownHostException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (_: SocketTimeoutException) {
            return Result.Error(NetworkError.REQUEST_TIMEOUT)
        } catch (_: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        } catch (_: ConnectException) {
            return Result.Error(NetworkError.CONNECTION_ERROR)
        } catch (_: Exception) {
            coroutineContext.ensureActive()
            return Result.Error(NetworkError.UNKNOWN)
        }
    }

    companion object {
        private val TAG = SearchRikMortiHeroesRepositoryImpl::class.java.simpleName
    }
}