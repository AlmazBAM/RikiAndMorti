package com.bagmanovam.rikiandmorti.presentation.home

sealed interface HomeScreenAction {

    data class OnQueryChange(val query: String): HomeScreenAction
    data object OnRefresh: HomeScreenAction
}