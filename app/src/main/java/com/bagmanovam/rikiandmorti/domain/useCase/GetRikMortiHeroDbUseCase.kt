package com.bagmanovam.rikiandmorti.domain.useCase

import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero

interface GetRikMortiHeroDbUseCase {
    suspend operator fun invoke(itemId: Int): RikMortiHero
}