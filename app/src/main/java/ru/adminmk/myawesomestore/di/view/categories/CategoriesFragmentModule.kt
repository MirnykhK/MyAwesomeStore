package ru.adminmk.myawesomestore.view

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import ru.adminmk.myawesomestore.di.viewmodel.ViewModelBuilder
import ru.adminmk.myawesomestore.di.viewmodel.ViewModelKey
import ru.adminmk.myawesomestore.view.categories.CategoriesFragment
import ru.adminmk.myawesomestore.viewmodel.categories.CategoriesLoaderViewModel

@Module
interface CategoriesFragmentModule {
    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    abstract fun injectCategoriesFragment(): CategoriesFragment

    @Binds
    @IntoMap
    @ViewModelKey(CategoriesLoaderViewModel::class)
    fun bindViewModel(viewModel: CategoriesLoaderViewModel): ViewModel
}
