package ru.adminmk.myawesomestore.viewmodel.main_page

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.adminmk.myawesomestore.repositories.remote_data.RemoteDataContract

class MainPageViewModelSavedStateFactory @AssistedInject constructor(
    private val remoteDataRepository: RemoteDataContract,
    @Assisted val owner: SavedStateRegistryOwner,
    @Assisted defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    @AssistedFactory
    interface ProviderFactory {
        fun create(
            @Assisted owner: SavedStateRegistryOwner,
            @Assisted defaultArgs: Bundle? = null
        ): MainPageViewModelSavedStateFactory
    }

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        @Suppress("UNCHECKED_CAST")
        return MainFragmentViewModel(
            handle,
            remoteDataRepository
        ) as T
    }
}
