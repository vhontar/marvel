package com.v7v.marvel.data

import android.content.Context
import androidx.room.Room
import com.v7v.marvel.data.local.MarvelDatabase
import com.v7v.marvel.data.local.dao.CharacterDao
import com.v7v.marvel.data.local.dao.ComicDao
import com.v7v.marvel.data.remote.MarvelCharactersService
import com.v7v.marvel.data.remote.MarvelComicsService
import com.v7v.marvel.data.remote.MarvelService
import com.v7v.marvel.data.repositories.MarvelCharactersRepositoryImpl
import com.v7v.marvel.data.repositories.MarvelComicsRepositoryImpl
import com.v7v.marvel.domain.repositories.MarvelCharactersRepository
import com.v7v.marvel.domain.repositories.MarvelComicsRepository
import com.v7v.marvel.logger.Logger
import com.v7v.marvel.logger.logDebug
import org.koin.core.scope.Scope
import org.koin.dsl.module

val dataModule = module {
    single<MarvelDatabase> { provideDatabase(get()) }
    single<CharacterDao> { provideCharacterDao(get()) }
    single<ComicDao> { provideComicDao(get()) }

    single<MarvelCharactersService> { provideMarvelService() }
    single<MarvelComicsService> { provideMarvelService() }

    single<MarvelCharactersRepository> { MarvelCharactersRepositoryImpl(get()) }
    single<MarvelComicsRepository> { MarvelComicsRepositoryImpl(get()) }
}

private fun provideMarvelService() = MarvelService(
    baseUrl = "", // TODO temporary
    publicToken = "", // TODO temporary
    privateToken = "",
    logging = { Logger.logDebug { it } },
)

private fun provideDatabase(context: Context): MarvelDatabase {
    return Room.databaseBuilder(
        context = context.applicationContext,
        klass = MarvelDatabase::class.java,
        name = "superhero_database"
    ).build()
}

private fun provideCharacterDao(database: MarvelDatabase) = database.characterDao()
private fun provideComicDao(database: MarvelDatabase) = database.comicDao()