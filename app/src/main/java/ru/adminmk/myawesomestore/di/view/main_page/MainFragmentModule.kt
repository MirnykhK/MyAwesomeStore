package ru.adminmk.myawesomestore.view

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import ru.adminmk.myawesomestore.di.viewmodel.ViewModelBuilder
import ru.adminmk.myawesomestore.di.viewmodel.ViewModelKey
import ru.adminmk.myawesomestore.view.main_page.MainFragment
import ru.adminmk.myawesomestore.viewmodel.main_page.MainFragmentViewModel

@Module
interface MainFragmentModule {
    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    abstract fun injectMainFragment(): MainFragment
}
