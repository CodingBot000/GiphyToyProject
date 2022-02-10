package com.exam.sample.viewmodel

import androidx.annotation.WorkerThread
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.exam.sample.domain.usecase.UseCaseGetTrendingData
import com.exam.sample.livedata.Event
import com.exam.sample.model.data.TrendingData
import com.exam.sample.model.data.TrendingDetail
import com.exam.sample.model.repository.trending.TrendingPagingRepository
import com.exam.sample.utils.Resource
import com.exam.sample.utils.isNetworkConnected
import com.exam.sample.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TrendingViewModel @Inject constructor(
    private val trendingPagingRepository: TrendingPagingRepository
    ) : BaseViewModel()
{
    private var job: Job? = null
    private var dataFlow: Flow<PagingData<TrendingDetail>> = flow { }
    val _dataListLiveData: MutableLiveData<PagingData<TrendingDetail>> = MutableLiveData()
    val dataListLiveData: LiveData<PagingData<TrendingDetail>>
        get() = _dataListLiveData

    @WorkerThread
    fun getTrendingData(rating: String = "") {
        if (!isNetworkConnected())
            return
        job?.cancel()
        job = viewModelScope.launch {
            dataFlow = trendingPagingRepository.getPagingData(rating)
            dataFlow.cachedIn(viewModelScope).collectLatest {
                _dataListLiveData.value = it
            }
        }
    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }
}
