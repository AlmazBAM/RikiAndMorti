package com.bagmanovam.rikiandmorti.presentation.person

import androidx.compose.runtime.Immutable
import com.bagmanovam.rikiandmorti.core.domain.NetworkError
import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero


@Immutable
data class PersonScreenState(
    val isLoading: Boolean = false,
    val errorMessage: NetworkError? = null,
    val rikMortiHero: RikMortiHero? = null,
)