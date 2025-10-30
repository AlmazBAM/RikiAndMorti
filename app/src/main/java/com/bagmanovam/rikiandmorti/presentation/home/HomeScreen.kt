package com.bagmanovam.rikiandmorti.presentation.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.bagmanovam.rikiandmorti.core.presentation.RikMortiHeroCard
import com.bagmanovam.rikiandmorti.core.presentation.SearchBar
import com.bagmanovam.rikiandmorti.core.presentation.utils.ObserveAsEvents
import com.bagmanovam.rikiandmorti.core.presentation.utils.toString
import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero
import kotlinx.coroutines.flow.Flow

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeScreenState,
    events: Flow<HomeScreenEvent>,
    onItemClick: (Int) -> Unit,
    onHomeAction: (HomeScreenAction) -> Unit,
    lazyPaddings: LazyPagingItems<RikMortiHero>,
) {
    val context = LocalContext.current
    val pullRefreshState = rememberPullToRefreshState()

    ObserveAsEvents(events = events) { event ->
        when (event) {
            is HomeScreenEvent.Error -> Toast.makeText(
                context,
                event.error.toString(context),
                Toast.LENGTH_LONG
            ).show()

            is HomeScreenEvent.Refresh -> {
                lazyPaddings.refresh()
            }
        }
    }

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
            when (uiState) {
                is HomeScreenState.Error -> {
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

                HomeScreenState.Loading -> {
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

                is HomeScreenState.Success -> {
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
                        onQueryChange = { onHomeAction(HomeScreenAction.OnQueryChange(it)) }
                    )
                    PullToRefreshBox(
                        modifier = Modifier.fillMaxSize(),
                        state = pullRefreshState,
                        isRefreshing = uiState.isSwipedToUpdate,
                        onRefresh = { onHomeAction(HomeScreenAction.OnRefresh) }
                    ) {
                        LazyVerticalGrid(
                            modifier = Modifier.fillMaxSize(),
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(vertical = 16.dp)
                        ) {
                            items(lazyPaddings.itemCount) { i ->
                                Log.e("TAG", "HomeScreen: ${lazyPaddings[i]}")
                                lazyPaddings[i]?.let {
                                    RikMortiHeroCard(
                                        modifier = Modifier.fillMaxWidth(),
                                        item = it,
                                        onItemClick = { onItemClick(it.id) }
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                            lazyPaddings.apply {
                                when (val append = loadState.append) {
                                    is LoadState.Loading -> {
                                        item(span = { GridItemSpan(2) }) {
                                            Box(
                                                Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                CircularProgressIndicator()
                                            }
                                        }
                                    }

                                    is LoadState.Error -> {
                                        item(span = { GridItemSpan(2) }) {
                                            Text(
                                                modifier = Modifier.clickable {
                                                    lazyPaddings.refresh()
                                                },
                                                text = "Ошибка подгрузки следующей страницы: ${append.error.message}",
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }

                                    else -> {}
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}