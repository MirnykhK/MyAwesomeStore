package ru.adminmk.myawesomestore.di.application

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ru.adminmk.myawesomestore.application.Application
import ru.adminmk.myawesomestore.di.model.ModelModule
import ru.adminmk.myawesomestore.di.view.MainActivityModule
import ru.adminmk.myawesomestore.view.CategoriesFragmentModule
import ru.adminmk.myawesomestore.view.MainFragmentModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        MainActivityModule::class,
        MainFragmentModule::class,
        CategoriesFragmentModule::class,
        ModelModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<Application> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}
