package co.geisyanne.volleymatch

import android.app.Application
import co.geisyanne.volleymatch.di.daoModule
import co.geisyanne.volleymatch.di.repositoryModule
import co.geisyanne.volleymatch.di.viewModelModule
import com.google.android.gms.ads.MobileAds
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(appModules)
        }

        MobileAds.initialize(this) {}
    }
}
val appModules = listOf(viewModelModule, repositoryModule, daoModule)