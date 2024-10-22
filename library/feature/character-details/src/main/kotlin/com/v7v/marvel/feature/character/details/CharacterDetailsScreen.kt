package com.v7v.marvel.feature.character.details

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import com.v7v.feature.character.details.R
import com.v7v.marvel.domain.models.Character
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterDetailsScreen(
    characterId: Int,
    viewModel: CharacterDetailsViewModel = koinViewModel(),
    onBackPressed: () -> Unit,
) {
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

        is State.Success -> CharacterDetailsContent(
            character = result.character,
            onBackPressed = onBackPressed,
            onFavoriteClick = { TODO() },
            onShareClick = { character ->
                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, character.name)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                context.startActivity(shareIntent)
            },
        )
    }

    LaunchedEffect(Unit) { viewModel.load(characterId) }
}

@Composable
fun CharacterDetailsContent(
    character: Character,
    onBackPressed: () -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClick: (Character) -> Unit,
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
                    .data(character.thumbnailUrl)
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
                    IconButton(onClick = { onShareClick(character) }) {
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
            text = character.name,
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
                    text = "Comics",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }
            items(character.comics.size) { index ->
                val comic = character.comics[index]
                ComicListItem(comic = comic)
            }
        }
    }
}

@Composable
fun ComicListItem(comic: Character.Comic) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = comic.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview
@Composable
private fun CharacterDetailsPreview() {
    CharacterDetailsScreen(
        characterId = 1,
        viewModel = object : CharacterDetailsViewModel() {
            override val state: StateFlow<State> = MutableStateFlow(
                State.Success(
                    character = Character(
                        id = 1,
                        name = "SpiderMan",
                        thumbnailUrl = "test.com",
                        comics = listOf(
                            Character.Comic(name = "Spider Boy"),
                            Character.Comic(name = "Spider Boy 2"),
                            Character.Comic(name = "Spider Boy 3"),
                            Character.Comic(name = "Spider Boy 4"),
                        ),
                    ),
                ),
            )

            override fun load(characterId: Int) {
            }
        },
        onBackPressed = {},
    )
}
