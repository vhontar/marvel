package com.v7v.marvel.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.v7v.feature.home.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onComicItemClicked: (Int) -> Unit,
    onCharacterItemClicked: (Int) -> Unit,
) {
    val comics = viewModel.comicsPagedFlow.collectAsLazyPagingItems()
    val characters = viewModel.charactersPagedFlow.collectAsLazyPagingItems()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(stringResource(R.string.comics_tab), stringResource(R.string.characters_tab))

    Scaffold(
        topBar = {
            Column {
                Image(
                    modifier = Modifier
                        .width(150.dp)
                        .padding(16.dp),
                    painter = painterResource(R.drawable.images),
                    contentDescription = "Marvel",
                )
                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(text = title) }
                        )
                    }
                }
            }
        },
        content = { paddingValues ->
            when (selectedTabIndex) {
                0 -> TabContent(
                    items = comics,
                    modifier = Modifier.padding(paddingValues),
                    listItem = { comic ->
                        ListItem(
                            item = comic,
                            text = { it.title },
                            imageUrl = { it.thumbnailUrl },
                            onItemClicked = { onComicItemClicked(it.id) },
                        )
                    },
                    onSearchQuerySubmitted = { query -> viewModel.searchComicsByTitle(query) },
                )

                1 -> TabContent(
                    items = characters,
                    modifier = Modifier.padding(paddingValues),
                    listItem = { character ->
                        ListItem(
                            item = character,
                            text = { it.name },
                            imageUrl = { it.thumbnailUrl },
                            onItemClicked = { onCharacterItemClicked(it.id) },
                        )
                    },
                    onSearchQuerySubmitted = { query -> viewModel.searchCharactersByName(query) },
                )
            }
        }
    )
}

@Composable
private fun <T : Any> TabContent(
    items: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    listItem: @Composable (T) -> Unit,
    onSearchQuerySubmitted: (String) -> Unit,
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val keyboard = LocalSoftwareKeyboardController.current

    val isLoading = items.loadState.refresh is LoadState.Loading
    val isError = items.loadState.refresh is LoadState.Error

    Column(modifier = modifier.padding(16.dp)) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.search_comics_title)) },
            enabled = true,
            value = searchQuery,
            onValueChange = { newValue -> searchQuery = newValue },
            textStyle = LocalTextStyle.current.copy(color = Color.Red),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchQuerySubmitted(searchQuery.text)
                    keyboard?.hide()
                },
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
                content = { CircularProgressIndicator() },
            )

            isError -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
                content = {
                    Text(
                        text = stringResource(id = R.string.error_loading_comics),
                        color = Color.Red
                    )
                }
            )

            else -> LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items.itemCount) {
                    items[it]?.let { item ->
                        listItem(item)
                    }
                }

                if (items.loadState.append is LoadState.Loading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun <T> ListItem(
    item: T,
    text: (T) -> String,
    imageUrl: (T) -> String,
    onItemClicked: (T) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth(),
        onClick = { onItemClicked(item) }
    ) {
        Column {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp)),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl(item))
                    .crossfade(true)
                    .build(),
                contentDescription = text(item),
                placeholder = painterResource(R.drawable.placeholder),
                error = painterResource(R.drawable.images),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.padding(8.dp, 0.dp),
                text = text(item),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(name = "HomeContent", group = "MarvelGroup")
@Composable
private fun HomeContentPreview() {
    HomeScreen(
        viewModel = MockHomeViewModel(),
        onComicItemClicked = {},
        onCharacterItemClicked = {},
    )
}