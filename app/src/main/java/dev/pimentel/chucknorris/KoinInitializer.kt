package dev.pimentel.chucknorris

import android.content.Context
import androidx.startup.Initializer
import dev.pimentel.chucknorris.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

@Suppress("Unused")
class KoinInitializer : Initializer<KoinApplication> {

    override fun create(context: Context): KoinApplication =
        startKoin {
            androidContext(context)
            loadKoinModules(appModules)
        }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
