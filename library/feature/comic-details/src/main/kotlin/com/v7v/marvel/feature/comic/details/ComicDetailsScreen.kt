package com.v7v.marvel.feature.comic.details

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.v7v.feature.comic.details.R
import com.v7v.marvel.domain.models.Comic
import org.koin.androidx.compose.koinViewModel

@Composable
fun ComicDetailsScreen(comicId: Int, viewModel: ComicDetailsViewModel = koinViewModel(), onBackPressed: () -> Unit) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()
    when (val result = state.value) {
        is State.Error -> Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) { Text(text = "An error occurred: ${result.message}") }

        State.Loading -> Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) { CircularProgressIndicator() }

        is State.Success -> ComicDetailsContent(
            comic = result.comic,
            onBackPressed = onBackPressed,
            onFavoriteClick = { TODO() },
            onShareClick = { comic ->
                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, comic.title)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                context.startActivity(shareIntent)
            },
        )
    }

    LaunchedEffect(Unit) { viewModel.load(comicId) }
}

@Composable
fun ComicDetailsContent(
    comic: Comic,
    onBackPressed: () -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClick: (Comic) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(comic.thumbnailUrl)
                    .build(),
                contentDescription = "Character Image",
                placeholder = painterResource(R.drawable.placeholder),
                error = painterResource(R.drawable.placeholder),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                        contentDescription = "Back",
                        tint = Color.White,
                    )
                }
                Row {
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.ic_favorite),
                            contentDescription = "Favorite",
                            tint = Color.White,
                        )
                    }
                    IconButton(onClick = { onShareClick(comic) }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.ic_share),
                            contentDescription = "Share",
                            tint = Color.White,
                        )
                    }
                }
            }
        }

        Text(
            text = comic.title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            item {
                Text(
                    text = "Characters",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }
            items(comic.characters.size) { index ->
                val character = comic.characters[index]
                CharacterListItem(character = character)
            }
        }
    }
}

@Composable
fun CharacterListItem(character: Comic.Character) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = character.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview
@Composable
private fun ComicDetailsPreview() {
    ComicDetailsScreen(
        comicId = 1,
        viewModel = MockComicDetailsViewModel(),
        onBackPressed = {},
    )
}
