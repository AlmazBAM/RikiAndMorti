package com.bagmanovam.rikiandmorti.presentation.home

sealed interface HomeEvent {

    data class OnQueryChange(val query: String): HomeEvent
    data object OnRefresh: HomeEvent
}