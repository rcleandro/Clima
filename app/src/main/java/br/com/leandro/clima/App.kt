package br.com.leandro.clima

import android.app.Application
import br.com.leandro.clima.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                listOf(
                    viewModelModule,
                    repositoryModule,
                    daoModule,
                    uiModule,
                    apiModule
                )
            )
        }
    }
}