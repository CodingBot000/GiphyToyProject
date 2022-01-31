package com.exam.sample.viewmodel

import androidx.lifecycle.*
import com.exam.sample.ui.state.UIState
import kotlinx.coroutines.flow.*


class MainSharedViewModel: ViewModel()
{
    private val _uiStateToActivity = MutableStateFlow<UIState>(UIState.INIT)
    val uiStateToActivity = _uiStateToActivity.asStateFlow()

    fun notiItemTabEvent() {
        _uiStateToActivity.value = UIState.INIT_UI_MAINACT
    }

    override fun onCleared() {
        super.onCleared()
    }
}
