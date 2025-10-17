package com.bagmanovam.rikiandmorti.di

import com.bagmanovam.rikiandmorti.presentation.home.HomeScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::HomeScreenViewModel)
}