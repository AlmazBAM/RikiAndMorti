package com.bagmanovam.rikiandmorti.domain.useCase

import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero

interface SaveRikMoritHeroesDbUseCase {
    suspend operator fun invoke(items: List<RikMortiHero>)
}