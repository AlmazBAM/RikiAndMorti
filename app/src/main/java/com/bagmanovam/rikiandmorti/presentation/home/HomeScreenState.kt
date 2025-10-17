package com.bagmanovam.rikiandmorti.presentation.home

import androidx.compose.runtime.Immutable
import com.bagmanovam.rikiandmorti.core.domain.NetworkError
import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero

@Immutable
data class HomeScreenState(
    val isLoading: Boolean = false,
    val isSwipedToUpdate: Boolean = false,
    val errorMessage: NetworkError? = null,
    val query: String = "",
    val rikMortiHeroes: List<RikMortiHero> = emptyList(),
)