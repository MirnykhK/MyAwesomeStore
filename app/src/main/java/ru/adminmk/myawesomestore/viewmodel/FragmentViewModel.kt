package ru.adminmk.myawesomestore.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.adminmk.myawesomestore.model.Result
import timber.log.Timber

abstract class FragmentViewModel<T> : ViewModel() {
    var job: Job? = null

    protected val _isDataAvailable = MutableLiveData<Boolean>()
    val isDataAvailable: LiveData<Boolean> = _isDataAvailable

    private val _remoteData = MutableLiveData<T?>()
    val remoteData: LiveData<T?> = _remoteData

    protected val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    fun loadRemoteData(forceRefresh: Boolean = false) {
        if (_isDataAvailable.value == true && !forceRefresh || _dataLoading.value == true) {
            return
        }

        job = viewModelScope.launch {
            _dataLoading.value = true

            withContext(Dispatchers.IO) {
                getRemoteData()
            }.let { result ->
                if (result is Result.Success) {
                    onRemoteDataLoaded(result.data)
                } else {
                    onDataNotAvailable(result)
                }
            }

            _dataLoading.value = false
        }
    }

    abstract suspend fun getRemoteData(): Result<T>

    protected fun setRemoteData(remoteData: T?) {
        this._remoteData.value = remoteData
        Timber.d("setRemoteData")
        _isDataAvailable.value = remoteData != null
    }

    private fun onRemoteDataLoaded(remoteData: T) {
        setRemoteData(remoteData)
    }

    private fun onDataNotAvailable(result: Result<T>) {
        _remoteData.value = null
        Timber.d("onDataNotAvailable")
        _isDataAvailable.value = false
    }

    fun cancelJob() {
        _dataLoading.value = false
        job?.cancel()
    }
}
