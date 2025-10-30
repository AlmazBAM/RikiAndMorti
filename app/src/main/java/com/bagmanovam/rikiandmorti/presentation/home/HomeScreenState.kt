package com.bagmanovam.rikiandmorti.presentation.home

import androidx.paging.PagingData
import com.bagmanovam.rikiandmorti.core.domain.NetworkError
import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero
import kotlinx.coroutines.flow.Flow

sealed interface HomeScreenState {
    object Loading : HomeScreenState
    data class Success(
        val isSwipedToUpdate: Boolean = false,
        val query: String = "",
        val errorMessage: NetworkError? = null,
    ) : HomeScreenState

    data class Error(val error: NetworkError) : HomeScreenState
}