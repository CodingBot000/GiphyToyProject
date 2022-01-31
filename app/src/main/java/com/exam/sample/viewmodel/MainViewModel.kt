package com.exam.sample.viewmodel


import android.util.Log
import com.exam.sample.domain.usecase.UseCaseGetTrendingData
import com.exam.sample.service.BackgroundAPICall
import com.exam.sample.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCaseGetTrendingData: UseCaseGetTrendingData
    ) : BaseViewModel()
{
    init {

        BackgroundAPICall.getInstance().useCaseGetTrendingData = useCaseGetTrendingData

    }

    override fun onCleared() {
        super.onCleared()
    }
}
