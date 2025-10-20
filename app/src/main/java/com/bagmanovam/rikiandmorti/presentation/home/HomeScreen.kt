package com.bagmanovam.rikiandmorti.presentation.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bagmanovam.rikiandmorti.core.presentation.RikMortiHeroCard
import com.bagmanovam.rikiandmorti.core.presentation.SearchBar
import com.bagmanovam.rikiandmorti.core.presentation.utils.toString

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeScreenState,
    onItemClick: (Int) -> Unit,
    onHomeAction: (HomeEvent) -> Unit,
) {
    val context = LocalContext.current

    val pullRefreshState = rememberPullToRefreshState()
    Log.e("TAG", "HomeScreen: ${uiState.isSwipedToUpdate}")
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter button"
                )
            }
        }
    ) { innerPAdding ->
        Column(
            modifier = modifier
                .padding(innerPAdding)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            SearchBar(
                query = uiState.query,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrectEnabled = true,
                    showKeyboardOnFocus = true,
                    hintLocales = LocaleList(Locale("ru"))
                ),
                onQueryChange = { onHomeAction(HomeEvent.OnQueryChange(it)) }
            )
            PullToRefreshBox(
                modifier = Modifier.fillMaxSize(),
                state = pullRefreshState,
                isRefreshing = uiState.isSwipedToUpdate,
                onRefresh = { onHomeAction(HomeEvent.OnRefresh) }
            ) {
                if (uiState.isLoading) {
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxSize(),
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        val items = uiState.rikMortiHeroes
                        if (items.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Ничего не найдено",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = 16.sp,
                                            lineHeight = 18.sp
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        } else {
                            itemsIndexed(items) { index, rikMortiHero ->
                                Log.e("TAG", "HomeScreen: $rikMortiHero", )
                                RikMortiHeroCard(
                                    modifier = Modifier,
                                    item = rikMortiHero,
                                    onItemClick = { onItemClick(index + 1) }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }

                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onBackground,
                            strokeWidth = 3.dp,
                        )
                    }
                }
            }
        }

        LaunchedEffect(uiState.errorMessage) {
            uiState.errorMessage?.let { error ->
                Toast.makeText(
                    context,
                    error.toString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}