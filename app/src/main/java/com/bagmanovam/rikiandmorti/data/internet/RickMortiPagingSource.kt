package com.bagmanovam.rikiandmorti.data.internet

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bagmanovam.rikiandmorti.data.mapper.toDomain
import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero

class RickMortiPagingSource(
    private val api: RikMortiApi,
) : PagingSource<Int, RikMortiHero>() {
    override fun getRefreshKey(state: PagingState<Int, RikMortiHero>): Int? {
        return state.anchorPosition?.let { anchor ->
            val page = state.closestPageToPosition(anchor)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RikMortiHero> {
        val page = params.key ?: 1
        return try {
            val response = api.getRikMortiHeroes(page)
            val items = when (response.code()) {
                in 200..299 -> {
                    try {
                        val bd = response.body()
                        bd?.results ?: emptyList()
                    } catch (_: Exception) {
                        emptyList()
                    }
                }
                else -> emptyList()

            }
            val nextKey = if (items.isEmpty()) {
                null
            } else {
                page + 1
            }

            val prevKey = if (page == 1) null else page - 1
            LoadResult.Page(
                data = items.map { it.toDomain() },
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}