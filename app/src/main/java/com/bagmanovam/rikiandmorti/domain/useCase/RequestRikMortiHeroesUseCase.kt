package com.bagmanovam.rikiandmorti.domain.useCase

import androidx.paging.PagingData
import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero
import kotlinx.coroutines.flow.Flow

interface RequestRikMortiHeroesUseCase {
    operator fun invoke(): Flow<PagingData<RikMortiHero>>
}