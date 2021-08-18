package ru.adminmk.myawesomestore.viewmodel.main_page

import androidx.lifecycle.SavedStateHandle
import ru.adminmk.myawesomestore.model.MainPageRemoteAnswer
import ru.adminmk.myawesomestore.repositories.remote_data.RemoteDataContract
import ru.adminmk.myawesomestore.viewmodel.FragmentViewModel
import javax.inject.Inject

class MainFragmentViewModel @Inject constructor(
    state: SavedStateHandle,
    private val remoteDataRepository: RemoteDataContract
) : FragmentViewModel<MainPageRemoteAnswer>()  {

    fun getListOSaleItems() = remoteDataRepository.getCacheMainPageRemoteAnswer().listOSaleItems
    fun getListONewItems() = remoteDataRepository.getCacheMainPageRemoteAnswer().listONewItems

    fun getSaleContentString() =
        remoteDataRepository.getCacheMainPageRemoteAnswer().saleContentString

    fun getNewContentString() = remoteDataRepository.getCacheMainPageRemoteAnswer().newContentString

    fun getBigBanner() = remoteDataRepository.getCacheMainPageRemoteAnswer().bigBanner

    override suspend fun getRemoteData() = remoteDataRepository.getMainPageData()
}

