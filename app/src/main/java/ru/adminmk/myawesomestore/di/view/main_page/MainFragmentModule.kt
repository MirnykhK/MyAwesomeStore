package ru.adminmk.myawesomestore.view

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.adminmk.myawesomestore.di.viewmodel.ViewModelBuilder
import ru.adminmk.myawesomestore.view.main_page.MainFragment

@Module
interface MainFragmentModule {
    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    abstract fun injectMainFragment(): MainFragment
}
