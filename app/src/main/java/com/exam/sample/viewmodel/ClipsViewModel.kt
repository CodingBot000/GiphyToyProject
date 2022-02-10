package com.exam.sample.viewmodel

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.exam.sample.domain.usecase.*
import com.exam.sample.livedata.Event
import com.exam.sample.model.data.TrendingData
import com.exam.sample.model.data.TrendingDetail
import com.exam.sample.model.repository.search.ClipPagingRepository
import com.exam.sample.model.repository.search.SearchPagingRepository
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
class ClipsViewModel @Inject constructor(
    private val clipsPagingRepository: ClipPagingRepository
) : BaseViewModel()
{
    private var job: Job? = null
    private var dataFlow: Flow<PagingData<TrendingDetail>> = flow { }
    val _dataListLiveData: MutableLiveData<PagingData<TrendingDetail>> = MutableLiveData()
    val dataListLiveData: LiveData<PagingData<TrendingDetail>>
        get() = _dataListLiveData

    @WorkerThread
    fun getClipsData(keyword: String) {
        if (!isNetworkConnected())
            return
        job?.cancel()
        job = viewModelScope.launch {
            dataFlow = clipsPagingRepository.getPagingData(keyword)
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
