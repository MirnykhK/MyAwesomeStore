package ru.adminmk.myawesomestore.viewmodel.main_page

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import ru.adminmk.myawesomestore.model.MainPageRemoteAnswer
import ru.adminmk.myawesomestore.repositories.remote_data.RemoteDataContract
import ru.adminmk.myawesomestore.viewmodel.FragmentViewModel
import javax.inject.Inject

private const val CACHE_STATE_KEY = "cacheStateKey"
private const val CACHE_BUNDLE_KEY = "cache"
private const val IS_DATA_AVAILABLE_BUNDLE_KEY = "isDataAvailable"

class MainFragmentViewModel @Inject constructor(
    val state: SavedStateHandle,
    private val remoteDataRepository: RemoteDataContract
) : FragmentViewModel<MainPageRemoteAnswer>() {

    init {
        state.get<Bundle?>(CACHE_STATE_KEY)?.let {
            remoteDataRepository.setCacheMainPageRemoteAnswer(it.getParcelable(CACHE_BUNDLE_KEY)!!)
            this._isDataAvailable.value = it.getBoolean(IS_DATA_AVAILABLE_BUNDLE_KEY)
        }
    }

    fun saveCache(outState: Bundle) {
        with(Bundle()) {
            putParcelable(CACHE_BUNDLE_KEY, remoteDataRepository.getCacheMainPageRemoteAnswer())
            putBoolean(IS_DATA_AVAILABLE_BUNDLE_KEY, isDataAvailable.value ?: false)

            state[CACHE_STATE_KEY] = this
        }
    }

    fun getListOSaleItems() = remoteDataRepository.getCacheMainPageRemoteAnswer().listOSaleItems
    fun getListONewItems() = remoteDataRepository.getCacheMainPageRemoteAnswer().listONewItems

    fun getSaleContentString() =
        remoteDataRepository.getCacheMainPageRemoteAnswer().saleContentString

    fun getNewContentString() = remoteDataRepository.getCacheMainPageRemoteAnswer().newContentString

    fun getBigBanner() = remoteDataRepository.getCacheMainPageRemoteAnswer().bigBanner

    override suspend fun getRemoteData() = remoteDataRepository.getMainPageData()
}
