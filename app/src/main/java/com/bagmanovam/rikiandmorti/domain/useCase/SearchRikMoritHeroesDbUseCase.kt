package com.bagmanovam.rikiandmorti.domain.useCase

import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero
import kotlinx.coroutines.flow.Flow

interface SearchRikMoritHeroesDbUseCase {
    suspend operator fun invoke(query: String): Flow<List<RikMortiHero>>
}