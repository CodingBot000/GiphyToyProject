package com.exam.sample.ui.state


sealed class UIState {
    object INIT: UIState()
    object INIT_UI_MAINACT: UIState()
    object REQ_REFRESH_DBLIST_FAVORITE: UIState()
    object ORDER_REFRESH_DBLIST_FAVORITE: UIState()
}
