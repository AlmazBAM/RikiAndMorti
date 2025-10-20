package com.bagmanovam.rikiandmorti.presentation.person

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.bagmanovam.rikiandmorti.core.presentation.utils.toString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonScreen(
    modifier: Modifier = Modifier,
    uiState: PersonScreenState,
    isMainScreen: Boolean,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    Log.e("TAG", "DescriptionScreen: $isMainScreen")
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            if (!isMainScreen) {
                CenterAlignedTopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                onBackClick()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBackIosNew,
                                contentDescription = "Back button"
                            )
                        }
                    }
                )
            }
        }
    ) { innerPaddings ->
        LaunchedEffect(uiState.errorMessage) {
            uiState.errorMessage?.let { error ->
                Toast.makeText(
                    context,
                    error.toString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        if (uiState.isLoading) {
            uiState.rikMortiHero?.let { rikMortiHero ->
                LazyColumn(
                    modifier = modifier
                        .padding(innerPaddings)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.FillWidth,
                            model = rikMortiHero.imageUrl,
                            contentDescription = "Item of the space objects"
                        )
                    }

                    item {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 24.sp,
                                lineHeight = 24.sp
                            ),
                            text = rikMortiHero.name,
                        )
                    }
                    item {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 16.sp,
                                lineHeight = 16.sp
                            ),
                            text = rikMortiHero.status,
                        )
                    }
                    item {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 16.sp,
                                lineHeight = 16.sp
                            ),
                            text = rikMortiHero.species,
                        )
                    }
                }
            } ?: run {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Данные не загружены",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 16.sp,
                            lineHeight = 18.sp
                        ),
                        textAlign = TextAlign.Center
                    )
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