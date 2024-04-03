package co.geisyanne.volleymatch

import android.app.Application
import co.geisyanne.volleymatch.di.daoModule
import co.geisyanne.volleymatch.di.repositoryModule
import co.geisyanne.volleymatch.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

// ponto de entrada no aplicativo
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(appModules)
        }
    }
}
val appModules = listOf(viewModelModule, repositoryModule, daoModule)