package com.bagmanovam.rikiandmorti.domain.useCase

import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero
import kotlinx.coroutines.flow.Flow

interface GetRikMoritHeroesDbUseCase {
    suspend operator fun invoke(): Flow<List<RikMortiHero>>
}