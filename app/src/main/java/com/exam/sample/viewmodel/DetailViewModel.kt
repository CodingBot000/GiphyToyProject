package com.exam.sample.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.exam.sample.domain.usecase.*
import com.exam.sample.livedata.Event
import com.exam.sample.model.data.DBResultData
import com.exam.sample.model.data.FavoriteInfo
import com.exam.sample.model.data.InteractionData
import com.exam.sample.model.data.TrendingData
import com.exam.sample.ui.state.CheckBoxState
import com.exam.sample.ui.state.DBState
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
    private val _favoriteCheckEvent = MutableLiveData<Event<CheckBoxState>>()
    val favoriteCheckEvent: LiveData<Event<CheckBoxState>> get() = _favoriteCheckEvent
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

    @WorkerThread
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

    @WorkerThread
    fun insertFavorite(favoriteInfo: FavoriteInfo) =
        with(useCaseDbInsert) {
            setData(favoriteInfo)
            execute(
                onSuccess = {
                    _dbEvent.postValue(Event(Resource.success(DBResultData(DBState.DB_INSERT, null, true))))
                },
                onError = {
                    _dbEvent.postValue(Event(Resource.error(it.message.toString(), DBResultData(DBState.DB_INSERT, null, false))))
                },
                onFinished = {
                }
            )
        }

    @WorkerThread
    fun removeFavorite(favoriteInfo: FavoriteInfo) =
        with(useCaseDbRemove) {
            setData(favoriteInfo)
            execute(
                onSuccess = {
                    _dbEvent.postValue(Event(Resource.success(DBResultData(DBState.DB_DELETE, null, true))))
                },
                onError = {
                    _dbEvent.postValue(Event(Resource.error(it.message.toString(), DBResultData(DBState.DB_DELETE, null, false))))
                },
                onFinished = {
                }
            )
        }

    @WorkerThread
    @SuppressLint("CheckResult")
    fun getFavorite(userId: String) =
        with(useCaseDbSelect) {
            setData(userId)
            execute(
                onSuccess = {
                    _dbEvent.postValue(Event(Resource.success(DBResultData(DBState.DB_SELECT, it, true))))
                },
                onError = {
                    _dbEvent.postValue(Event(Resource.error(it.message.toString(), DBResultData(DBState.DB_SELECT, it, false))))
                },
                onFinished = {
                }
            )
        }

    fun checkBoxChecked(isChecked: Boolean, compoundButton: CompoundButton) {
        val state = when {
            (isChecked && compoundButton.isPressed) -> CheckBoxState.PRESSED_CHECK
            (!isChecked && compoundButton.isPressed) -> CheckBoxState.PRESSED_UNCHECK
            (isChecked && !compoundButton.isPressed) -> CheckBoxState.INIT_CHECK
            (!isChecked && !compoundButton.isPressed) -> CheckBoxState.INIT_UNCHECK
            else -> CheckBoxState.INIT
        }

        _favoriteCheckEvent.value = Event(state)
    }

    fun btnClickEventSend(index: Int) {
        _btnSimpleEvent.value = Event(index)
    }

    override fun onCleared() {
        super.onCleared()
    }
}
