package ru.adminmk.myawesomestore.viewmodel.host

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.adminmk.myawesomestore.viewmodel.Event

class HostViewModel : ViewModel() {
    private val _navigationState = MutableLiveData<Event<Int>>()
    val navigationState: LiveData<Event<Int>> = _navigationState

    private val _statusBarHeight = MutableLiveData<Event<Int>>()
    val statusBarHeight: LiveData<Event<Int>> = _statusBarHeight

    var isAppLaunched = false

    fun navigate(fragmentDestinationId: Int) {
        _navigationState.postValue(Event(fragmentDestinationId))
    }

    fun initStatusBarHeight(height: Int) {
        _statusBarHeight.value?.let {
            if (it.peekContent() == height) return
        }
        _statusBarHeight.postValue(Event(height))
    }
}
