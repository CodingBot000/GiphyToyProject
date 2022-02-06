package com.exam.sample.model.data

import com.exam.sample.ui.state.DBState

/**
 * Database ResultData
 */
data class DBResultData(val flag: DBState, val data: Any?, val result: Boolean)
