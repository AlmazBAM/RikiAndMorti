package com.bagmanovam.rikiandmorti.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.room.util.query
import com.bagmanovam.rikiandmorti.core.domain.NetworkError
import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero
import com.bagmanovam.rikiandmorti.domain.useCase.GetRikMoritHeroesDbUseCase
import com.bagmanovam.rikiandmorti.domain.useCase.RequestRikMortiHeroesUseCase
import com.bagmanovam.rikiandmorti.domain.useCase.SearchRikMoritHeroesDbUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val requestUseCase: RequestRikMortiHeroesUseCase,
    private val getDbUseCase: GetRikMoritHeroesDbUseCase,
    private val searchDbUseCase: SearchRikMoritHeroesDbUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
    val uiState = _uiState.asStateFlow()
    private val _events = Channel<HomeScreenEvent>()

    val events = _events.receiveAsFlow()
    private val searchQueryFlow = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val heroesFlow: Flow<PagingData<RikMortiHero>> =
        searchQueryFlow
            .debounce(300)
            .flatMapLatest { query ->
                if (query.isBlank())
                    requestUseCase()
                else
                    searchDbUseCase(query).map { PagingData.from(it) }
            }.cachedIn(viewModelScope)
            .onStart {
                _uiState.value = HomeScreenState.Loading
            }
            .onEach {
                _uiState.update {
                    HomeScreenState.Success(
                        isSwipedToUpdate = false
                    )
                }
            }
            .catch {
                Log.e(TAG, "catched ${it.message} ")
                _events.send(HomeScreenEvent.Error(NetworkError.SERVER_ERROR))
            }


//    @OptIn(ExperimentalCoroutinesApi::class)
//    val heroesFlow: Flow<PagingData<RikMortiHero>> =
//        combine(searchQueryFlow, getDbUseCase()) { query, localCache ->
//            query to localCache
//        }.flatMapLatest { (query, localCache) ->
//            flow {
//                if (query.isBlank() && localCache.isNotEmpty()) {
//                    emit(PagingData.from(localCache))
//                } else if (query.isNotBlank()) {
//                    val filtered = searchDbUseCase(query).firstOrNull().orEmpty()
//                    emit(PagingData.from(filtered))
//                }
//                emitAll(networkPagingFlow)
//            }
//        }.onStart {
//            _uiState.value = HomeScreenState.Loading
//        }.onEach {
//            _uiState.update {
//                HomeScreenState.Success(
//                    isSwipedToUpdate = false
//                )
//            }
//        }.catch {
//            Log.e(TAG, "catched ${it.message} ", )
//            _events.send(HomeScreenEvent.Error(NetworkError.SERVER_ERROR))
//        }.flowOn(Dispatchers.IO)
//

    fun onAction(event: HomeScreenAction) {
        when (event) {
            is HomeScreenAction.OnQueryChange -> {
                searchQueryFlow.update { event.query.trim() }
                _uiState.update { state ->
                    if (state is HomeScreenState.Success)
                        state.copy(query = event.query)
                    else
                        state
                }
            }

            HomeScreenAction.OnRefresh -> {
                _uiState.update { state ->
                    if (state is HomeScreenState.Success)
                        state.copy(
                            isSwipedToUpdate = true
                        )
                    else state
                }
                viewModelScope.launch {
                    _events.send(HomeScreenEvent.Refresh)
                    _uiState.update { state ->
                        if (state is HomeScreenState.Success) {
                            HomeScreenState.Success(
                                isSwipedToUpdate = false,
                            )
                        } else {
                            state
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