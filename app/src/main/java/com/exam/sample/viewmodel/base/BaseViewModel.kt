package com.exam.sample.viewmodel.base

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exam.sample.App
import com.exam.sample.livedata.Event
import com.exam.sample.utils.Const

abstract class BaseViewModel() : ViewModel() {
    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> get() = _isLoading

    override fun onCleared() {
        super.onCleared()
    }

    protected fun showProgress() {
        Log.v(Const.LOG_TAG, "App.getLifeCycleApplication() showProgressaadsdafsadfs")
        Log.v(Const.LOG_TAG, "App.getLifeCycleApplication() showProgress:${App.getLifeCycleApplication()}")
        if (App.getLifeCycleApplication() <= Lifecycle.Event.ON_RESUME)
            _isLoading.value = Event(true)
    }

    protected fun hideProgress() {
        Log.v(Const.LOG_TAG, "App.getLifeCycleApplication() hideProgress:${App.getLifeCycleApplication()}")
        if (App.getLifeCycleApplication() <= Lifecycle.Event.ON_PAUSE)
            _isLoading.value = Event(false)
    }
}
