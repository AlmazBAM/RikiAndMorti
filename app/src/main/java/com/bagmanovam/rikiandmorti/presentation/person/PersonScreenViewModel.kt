package com.bagmanovam.rikiandmorti.presentation.person

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagmanovam.nasa_planets.core.domain.onError
import com.bagmanovam.nasa_planets.core.domain.onSuccess
import com.bagmanovam.rikiandmorti.domain.useCase.GetRikMortiHeroDbUseCase
import com.bagmanovam.rikiandmorti.domain.useCase.RequestRikMortiHeroUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PersonScreenViewModel(
    private val requestUseCase: RequestRikMortiHeroUseCase,
    private val getDbUseCase: GetRikMortiHeroDbUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PersonScreenState())
    val uiState = _uiState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            PersonScreenState()
        )


    fun getSpaceItemById(itemId: Int) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isLoading = false
                )
            }
            requestUseCase(itemId)
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            errorMessage = null,
                            rikMortiHero = it,
                            isLoading = true
                        )
                    }
                }
                .onError { networkError ->
                    runCatching {
                        getDbUseCase(itemId)
                    }.onSuccess {
                        Log.i("TAG", "getSpaceItemById: $itemId")
                        _uiState.update { state ->
                            state.copy(
                                errorMessage = null,
                                rikMortiHero = it,
                                isLoading = true
                            )
                        }
                    }.onFailure {
                        Log.i("TAG", "getSpaceItemById: $itemId")
                        _uiState.update { state ->
                            state.copy(
                                errorMessage = networkError,
                                rikMortiHero = null,
                                isLoading = true
                            )
                        }
                    }
                }
        }
    }

    companion object {
        private val TAG = PersonScreenViewModel::class.java.simpleName
    }
}