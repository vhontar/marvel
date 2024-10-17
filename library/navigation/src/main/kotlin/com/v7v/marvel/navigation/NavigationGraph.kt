package com.v7v.marvel.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.v7v.marvel.feature.character.details.CharacterDetailsScreen
import com.v7v.marvel.feature.home.HomeScreen

@Composable
internal fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination
    ) {
        composable<HomeDestination> {
            HomeScreen(
                onComicItemClicked = { comicId -> navController.navigate(ComicDetailsScreen(comicId)) },
                onCharacterItemClicked = { characterId ->
                    navController.navigate(
                        CharacterDetailsDestination(characterId)
                    )
                }
            )
        }
        composable<CharacterDetailsDestination> { navBackStackEntry ->
            val destination: CharacterDetailsDestination = navBackStackEntry.toRoute()
            CharacterDetailsScreen(
                characterId = destination.characterId,
                onBackPressed = { navController.popBackStack() },
            )
        }
    }
}