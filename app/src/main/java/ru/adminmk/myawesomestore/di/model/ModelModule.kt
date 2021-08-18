package ru.adminmk.myawesomestore.di.model

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.adminmk.myawesomestore.repositories.local_settings.LocalSettingsContract
import ru.adminmk.myawesomestore.repositories.local_settings.LocalSettingsImpl
import ru.adminmk.myawesomestore.repositories.remote_data.RemoteDataContract
import ru.adminmk.myawesomestore.repositories.remote_data.RemoteDataFakeImpl
import javax.inject.Singleton

@Module
object ModelModule {
    @Singleton
    @Provides
    fun provideLocalSettingsRepository(
        context: Context
    ): LocalSettingsContract = LocalSettingsImpl(context)

    @Singleton
    @Provides
    fun provideRemoteDataRepository(
        context: Context
    ): RemoteDataContract = RemoteDataFakeImpl(context)
}
