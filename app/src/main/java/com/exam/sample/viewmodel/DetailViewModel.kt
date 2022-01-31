package com.exam.sample.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.exam.sample.domain.usecase.*
import com.exam.sample.livedata.Event
import com.exam.sample.model.data.DBResultData
import com.exam.sample.model.data.FavoriteInfo
import com.exam.sample.model.data.InteractionData
import com.exam.sample.model.data.TrendingData
import com.exam.sample.utils.Const
import com.exam.sample.utils.Resource
import com.exam.sample.utils.isNetworkConnected
import com.exam.sample.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val useCaseDbSelect: UseCaseDbSelect,
    private val useCaseDbRemove: UseCaseDbRemove,
    private val useCaseDbInsert: UseCaseDbInsert,
    private val useCaseGetDetailData: UseCaseGetDetailData
) : BaseViewModel()
{
    private val _dbEvent = MutableLiveData<Event<Resource<DBResultData>>>()
    val dbEvent: LiveData<Event<Resource<DBResultData>>> get() = _dbEvent
    private val _favoriteCheckEvent = MutableLiveData<Event<Boolean>>()
    val favoriteCheckEvent: LiveData<Event<Boolean>> get() = _favoriteCheckEvent
    private val _btnSimpleEvent = MutableLiveData<Event<Int>>()
    val btnSimpleEvent: LiveData<Event<Int>> get() = _btnSimpleEvent

    private val _itemLiveData = MutableLiveData<Event<Resource<TrendingData>>>()
    val itemLiveData: LiveData<Event<Resource<TrendingData>>> get() = _itemLiveData

    var interactionData: InteractionData? = null

    fun showProgressForView() {
        showProgress()
    }

    fun hideProgressForView() {
        hideProgress()
    }

    fun getDetailData(id: String) {
        if (!isNetworkConnected())
            return

        showProgress()
        with(useCaseGetDetailData) {
            setData(id)
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

    fun insertFavorite(favoriteInfo: FavoriteInfo) =
        with(useCaseDbInsert) {
            setData(favoriteInfo)
            execute(
                onSuccess = {
                    _dbEvent.postValue(Event(Resource.success(DBResultData(Const.DB_INSERT, null, true))))
                },
                onError = {
                    _dbEvent.postValue(Event(Resource.error(it.message.toString(), DBResultData(Const.DB_INSERT, null, false))))
                },
                onFinished = {
                }
            )
        }

    fun removeFavorite(favoriteInfo: FavoriteInfo) =
        with(useCaseDbRemove) {
            setData(favoriteInfo)
            execute(
                onSuccess = {
                    _dbEvent.postValue(Event(Resource.success(DBResultData(Const.DB_DELETE, null, true))))
                },
                onError = {
                    _dbEvent.postValue(Event(Resource.error(it.message.toString(), DBResultData(Const.DB_DELETE, null, false))))
                },
                onFinished = {
                }
            )
        }

    @SuppressLint("CheckResult")
    fun getFavorite(userId: String) =
        with(useCaseDbSelect) {
            setData(userId)
            execute(
                onSuccess = {
                    _dbEvent.postValue(Event(Resource.success(DBResultData(Const.DB_SELECT, it, true))))
                },
                onError = {
                    _dbEvent.postValue(Event(Resource.error(it.message.toString(), DBResultData(Const.DB_SELECT, it, false))))
                },
                onFinished = {
                }
            )
        }

    fun checkBoxChecked(b: Boolean) {
        _favoriteCheckEvent.value = Event(b)
    }

    fun btnClickEventSend(index: Int) {
        _btnSimpleEvent.value = Event(index)
    }

    override fun onCleared() {
        super.onCleared()
    }
}
