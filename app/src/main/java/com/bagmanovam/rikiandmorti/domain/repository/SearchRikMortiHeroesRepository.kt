package com.bagmanovam.rikiandmorti.domain.repository

import androidx.paging.PagingData
import com.bagmanovam.rikiandmorti.core.domain.Result
import com.bagmanovam.rikiandmorti.core.domain.NetworkError
import com.bagmanovam.rikiandmorti.data.model.RikMortiHeroesDto
import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero
import kotlinx.coroutines.flow.Flow

interface SearchRikMortiHeroesRepository {
    fun requestRikMortiHeroes(): Flow<PagingData<RikMortiHero>>
    suspend fun requestRikMortiHero(itemId: Int): Result<RikMortiHero, NetworkError>
}