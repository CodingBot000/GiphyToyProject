package com.exam.sample.ui.state


sealed class DBState {
    object DB_SELECT: DBState()
    object DB_INSERT: DBState()
    object DB_DELETE: DBState()
    object DB_DELETEALL: DBState()
}
