package ru.adminmk.myawesomestore.application

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import ru.adminmk.myawesomestore.BuildConfig
import ru.adminmk.myawesomestore.di.application.DaggerApplicationComponent
import timber.log.Timber

class Application : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseLogger())
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.factory().create(this)
    }
}
