package com.bagmanovam.rikiandmorti

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bagmanovam.rikiandmorti.presentation.home.HomeScreen
import com.bagmanovam.rikiandmorti.presentation.home.HomeScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RikMortiNavHost(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Home
    ) {
        composable<Home> {
            val homeViewModel = koinViewModel<HomeScreenViewModel>(viewModelStoreOwner = it)
            val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

            HomeScreen(
                uiState = uiState,
                onItemClick = {},
                onHomeAction = homeViewModel::onAction
            )
        }
    }
}