package com.bagmanovam.rikiandmorti.presentation.home

import com.bagmanovam.rikiandmorti.core.domain.NetworkError

sealed interface HomeScreenEvent {

    data class Error(val error: NetworkError): HomeScreenEvent
    object Refresh: HomeScreenEvent
}