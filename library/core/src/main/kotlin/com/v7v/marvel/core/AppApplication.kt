package com.v7v.marvel.core

import android.app.Application
import com.v7v.marvel.data.dataModule
import com.v7v.marvel.feature.character.details.characterDetailsViewModelModule
import com.v7v.marvel.feature.comic.details.comicDetailsViewModelModule
import com.v7v.marvel.feature.home.homeViewModelModule
import com.v7v.marvel.logger.Logger
import com.v7v.marvel.logger.Severity
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

object AppApplication {
    fun init(application: Application) {
        Logger.sendTo(
            destination = object : Logger.Destination {
                override fun isLoggable(severity: Severity): Boolean = true

                override fun logInfo(tag: String, message: String) {
                    println("$tag: $message")
                }

                override fun logDebug(tag: String, message: String) {
                    println("$tag: $message")
                }

                override fun logWarn(tag: String, message: String?, throwable: Throwable?) {
                    println("$tag: $message")
                    println("$tag: $throwable")
                }

                override fun logError(tag: String, message: String?, throwable: Throwable?) {
                    println("$tag: $message")
                    println("$tag: $throwable")
                }
            },
        )
        startKoin {
            androidLogger()
            androidContext(application)
            modules(
                dataModule,
                homeViewModelModule,
                characterDetailsViewModelModule,
                comicDetailsViewModelModule,
            )
        }
    }
}
