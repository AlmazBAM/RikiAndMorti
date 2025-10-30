package com.bagmanovam.rikiandmorti

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import com.bagmanovam.rikiandmorti.presentation.home.HomeScreen
import com.bagmanovam.rikiandmorti.presentation.home.HomeScreenViewModel
import com.bagmanovam.rikiandmorti.presentation.person.PersonScreen
import com.bagmanovam.rikiandmorti.presentation.person.PersonScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RikMortiNavHost(
    navHostController: NavHostController,
) {
    NavHost(
        navController = navHostController,
        startDestination = Home
    ) {
        composable<Home> {
            val homeViewModel = koinViewModel<HomeScreenViewModel>(viewModelStoreOwner = it)
            val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
            val lazyPaddings = homeViewModel.heroesFlow.collectAsLazyPagingItems()

            HomeScreen(
                uiState = uiState,
                events = homeViewModel.events,
                lazyPaddings = lazyPaddings,
                onItemClick = { itemId ->
                    navHostController.navigate(Description(itemId))
                },
                onHomeAction = homeViewModel::onAction,
            )
        }

        composable<Description> {
            val viewModel = koinViewModel<PersonScreenViewModel>(viewModelStoreOwner = it)
            val itemId = it.toRoute<Description>().itemId
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val isMainScreen = it.destination.hasRoute(Home::class)

            LaunchedEffect(itemId) {
                viewModel.getSpaceItemById(itemId)
            }

            PersonScreen(
                uiState = uiState,
                isMainScreen = isMainScreen,
                onBackClick = {
                    navHostController.popBackStack()
                }
            )
        }
    }
}