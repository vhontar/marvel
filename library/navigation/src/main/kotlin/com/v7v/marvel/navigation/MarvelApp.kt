package com.v7v.marvel.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MarvelApp() {
    val navController = rememberNavController()
    NavigationGraph(navController)
}