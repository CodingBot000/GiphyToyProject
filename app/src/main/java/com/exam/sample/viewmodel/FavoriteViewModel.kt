package com.exam.sample.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.exam.sample.domain.usecase.*
import com.exam.sample.livedata.Event
import com.exam.sample.model.data.DBResultData
import com.exam.sample.model.data.FavoriteInfo
import com.exam.sample.model.data.TrendingData
import com.exam.sample.ui.state.DBState
import com.exam.sample.utils.Const
import com.exam.sample.utils.Resource
import com.exam.sample.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ActivityRetainedScoped
import java.lang.StringBuilder
import javax.inject.Inject


@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val useCaseGetGIFsByIds: UseCaseGetGIFsByIds,
    private val useCaseDbSelectAll: UseCaseDbSelectAll,
    private val useCaseDbRemoveAll: UseCaseDbRemoveAll
    ) : BaseViewModel()
{
    private val _dbEvent = MutableLiveData<Event<Resource<DBResultData>>>()
    val dbEvent: LiveData<Event<Resource<DBResultData>>> get() = _dbEvent

    private val _itemLiveData = MutableLiveData<Event<Resource<TrendingData>>>()
    val itemLiveData: LiveData<Event<Resource<TrendingData>>> get() = _itemLiveData

    @WorkerThread
    fun getFavoriteInfoRequest(list: List<FavoriteInfo>) {
        if (list.isEmpty())
            return

        val sb = StringBuilder()
        sb.apply {
            list.forEach {
                append("${it.userId},")
            }
            deleteCharAt(lastIndex)
        }

        showProgress()
        with(useCaseGetGIFsByIds) {
            setData(sb.toString())
            execute(
                onSuccess = {
                    _itemLiveData.postValue(Event(Resource.success(it)))
                },
                onError = {
                    _itemLiveData.postValue(Event(Resource.error(it.message.toString(), null)))
                },
                onFinished = {
                    hideProgress()
                }
            )
        }
    }

    @WorkerThread
    @SuppressLint("CheckResult")
    fun getFavoriteAll() {
        showProgress()
        useCaseDbSelectAll.execute(
            onSuccess = {
                _dbEvent.postValue(Event(Resource.success(DBResultData(DBState.DB_SELECT, it, true))))
            },
            onError = {
                _dbEvent.postValue(Event(Resource.error(it.message.toString(), DBResultData(DBState.DB_SELECT, null, false))))
            },
            onFinished = {
                hideProgress()
            }
        )
    }

    @WorkerThread
    fun removeAllFavoriteDB() {
        useCaseDbRemoveAll.execute(
            onSuccess = {
                _dbEvent.postValue(Event(Resource.success(DBResultData(DBState.DB_DELETE, it, true))))
            },
            onError = {
                _dbEvent.postValue(Event(Resource.error(it.message.toString(), DBResultData(DBState.DB_DELETE, null, false))))
            },
            onFinished = {
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
    }
}
