package com.bagmanovam.rikiandmorti.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagmanovam.nasa_planets.core.domain.onError
import com.bagmanovam.nasa_planets.core.domain.onSuccess
import com.bagmanovam.rikiandmorti.domain.useCase.GetRikMoritHeroesDbUseCase
import com.bagmanovam.rikiandmorti.domain.useCase.RequestRikMortiHeroesUseCase
import com.bagmanovam.rikiandmorti.domain.useCase.SaveRikMoritHeroesDbUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val requestUseCase: RequestRikMortiHeroesUseCase,
    private val saveDbUseCase: SaveRikMoritHeroesDbUseCase,
    private val getDbUseCase: GetRikMoritHeroesDbUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenState())
    val uiState = _uiState
        .onStart {
            val items = getDbUseCase().first()
            _uiState.update { state ->
                state.copy(
                    errorMessage = null,
                    isLoading = true,
                    isSwipedToUpdate = false,
                    spaceItems = items
                )
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            HomeScreenState()
        )


    fun onAction(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnQueryChange -> {
                Log.i(TAG, "query changed: ${event.query}")
            }

            HomeEvent.OnRefresh -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSwipedToUpdate = true
                    )
                }
                viewModelScope.launch {
                    requestUseCase(20)
                        .onSuccess {
                            Log.e(TAG, "onSuccess: ")
                            _uiState.update { state ->
                                state.copy(
                                    errorMessage = null,
                                    isLoading = true,
                                    isSwipedToUpdate = false
                                )
                            }
                            saveDbUseCase(it)
                        }
                        .onError {
                            Log.e(TAG, "onError: ")
                            val items = getDbUseCase().first()
                            _uiState.update { state ->
                                state.copy(
                                    errorMessage = it,
                                    isLoading = true,
                                    isSwipedToUpdate = false,
                                    spaceItems = items
                                )
                            }
                        }
                }
            }
        }
    }

    companion object {
        private val TAG = HomeScreenViewModel::class.java.simpleName
    }
}