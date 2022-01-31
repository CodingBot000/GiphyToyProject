package com.exam.sample.ui.state


sealed class UIState {
    object INIT: UIState()
    object INIT_UI_MAINACT: UIState()
}
