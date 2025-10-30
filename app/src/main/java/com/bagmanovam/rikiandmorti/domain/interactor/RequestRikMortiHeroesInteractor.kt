package com.bagmanovam.rikiandmorti.domain.interactor

import androidx.paging.PagingData
import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero
import com.bagmanovam.rikiandmorti.domain.repository.SearchRikMortiHeroesRepository
import com.bagmanovam.rikiandmorti.domain.useCase.RequestRikMortiHeroesUseCase
import kotlinx.coroutines.flow.Flow

class RequestRikMortiHeroesInteractor(private val repository: SearchRikMortiHeroesRepository) :
    RequestRikMortiHeroesUseCase {
    override fun invoke(): Flow<PagingData<RikMortiHero>> {
        return repository.requestRikMortiHeroes()
    }
}