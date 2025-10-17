package com.bagmanovam.rikiandmorti.presentation.home

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bagmanovam.rikiandmorti.core.presentation.SearchBar
import com.bagmanovam.rikiandmorti.core.presentation.utils.toString
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeScreenState,
    onItemClick: (Int) -> Unit,
    onHomeAction: (HomeEvent) -> Unit,
) {
    val context = LocalContext.current
    val refreshState = rememberSwipeRefreshState(uiState.isSwipedToUpdate)


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
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                SwipeRefresh(
                    state = refreshState,
                    onRefresh = { onHomeAction(HomeEvent.OnRefresh) }
                ) {
                    if (uiState.isLoading) {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 150.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            itemsIndexed(uiState.spaceItems) { index, spaceObject ->
                                Box(
                                    modifier = Modifier.aspectRatio(1f)
                                ) {
                                    AsyncImage(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(16.dp))
                                            .clickable {
                                                onItemClick(index)
                                            },
                                        contentScale = ContentScale.Crop,
                                        model = spaceObject.imageUrl,
                                        contentDescription = "Item of the space objects"
                                    )
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
        }
    }
}