package ru.adminmk.myawesomestore.viewmodel.categories

import ru.adminmk.myawesomestore.model.CategoriesRemoteAnswer
import ru.adminmk.myawesomestore.model.Result
import ru.adminmk.myawesomestore.model.TopCategorie
import ru.adminmk.myawesomestore.repositories.local_settings.LocalSettingsContract
import ru.adminmk.myawesomestore.repositories.remote_data.RemoteDataContract
import ru.adminmk.myawesomestore.viewmodel.FragmentViewModel
import javax.inject.Inject

class CategoriesLoaderViewModel @Inject constructor(
    private val localSettingsRepository: LocalSettingsContract,
    private val remoteDataRepository: RemoteDataContract
) : FragmentViewModel<CategoriesRemoteAnswer>() {

    init {
        remoteDataRepository.getCacheCategoriesRemoteAnswer()?.let {
            setRemoteData(it)
            _dataLoading.value = false
        }
    }

    fun getTopCategory() = localSettingsRepository.getTopCategory()
    fun saveTopCategory(topCategorie: TopCategorie) {
        localSettingsRepository.saveTopCategory(topCategorie)
    }

    override suspend fun getRemoteData(): Result<CategoriesRemoteAnswer>
        =  remoteDataRepository.getCategoriesData()
}
