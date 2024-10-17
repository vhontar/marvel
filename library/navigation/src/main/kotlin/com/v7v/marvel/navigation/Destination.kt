package com.v7v.marvel.navigation

import kotlinx.serialization.Serializable

@Serializable
data object HomeDestination

@Serializable
data class CharacterDetailsDestination(val characterId: Int)

@Serializable
data class ComicDetailsScreen(val id: Int)

