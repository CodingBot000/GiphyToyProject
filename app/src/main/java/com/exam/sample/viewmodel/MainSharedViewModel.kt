package com.exam.sample.viewmodel

import androidx.lifecycle.*
import com.exam.sample.ui.state.UIState
import kotlinx.coroutines.flow.*


class MainSharedViewModel: ViewModel()
{
    private val _uiStateToActivity = MutableStateFlow<UIState>(UIState.INIT)
    val uiStateToActivity = _uiStateToActivity.asStateFlow()

    private val _uiStateToFragment = MutableStateFlow<UIState>(UIState.INIT)
    val uiStateToFragment = _uiStateToFragment.asStateFlow()

    fun notiItemTabEvent() {
        _uiStateToActivity.value = UIState.INIT_UI_MAINACT
    }

    fun notiFavoriteDBListRefreshEventToActivity() {
        _uiStateToActivity.value = UIState.REQ_REFRESH_DBLIST_FAVORITE
    }

    fun notiFavoriteDBListRefreshEventToFavoriteView() {
        _uiStateToFragment.value = UIState.ORDER_REFRESH_DBLIST_FAVORITE
    }

    override fun onCleared() {
        super.onCleared()
    }
}
