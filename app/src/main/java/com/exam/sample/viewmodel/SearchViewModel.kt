package com.exam.sample.viewmodel

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.exam.sample.domain.usecase.UseCaseGetSearchData
import com.exam.sample.domain.usecase.UseCaseGetTrendingData
import com.exam.sample.livedata.Event
import com.exam.sample.model.data.TrendingData
import com.exam.sample.model.data.TrendingDetail
import com.exam.sample.model.repository.search.SearchPagingRepository
import com.exam.sample.model.repository.search.SearchRepository
import com.exam.sample.utils.Const
import com.exam.sample.utils.Resource
import com.exam.sample.utils.isNetworkConnected
import com.exam.sample.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor (
    private val searchPagingRepository: SearchPagingRepository
    ) : BaseViewModel()
{
    private val textChangeSubject = PublishSubject.create<String>()

    init {
        textChangeSubject.onNext("")
    }

    private var job: Job? = null
    private var dataFlow: Flow<PagingData<TrendingDetail>> = flow { }
    var dataListLiveData: MutableLiveData<PagingData<TrendingDetail>> = MutableLiveData()

    @WorkerThread
    fun getSearchData(keyword: String) {
        if (!isNetworkConnected())
            return
        job?.cancel()
        job = viewModelScope.launch {
            dataFlow = searchPagingRepository.getPagingData(keyword)
            dataFlow.cachedIn(viewModelScope).collectLatest {
                dataListLiveData.value = it
            }
        }
    }

    fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        textChangeSubject.apply {
            if (s.isEmpty())
                onNext("")
            else
                onNext(s.toString())
        }
    }

    fun getSearchKeyChangeObserver(): Observable<String> {
        return textChangeSubject.debounce(Const.EMIT_INTERVAL, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }
}
