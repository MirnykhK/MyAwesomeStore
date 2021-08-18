package ru.adminmk.myawesomestore.di.view

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.adminmk.myawesomestore.view.MainActivity

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector
    abstract fun injectMainActivity(): MainActivity
}
