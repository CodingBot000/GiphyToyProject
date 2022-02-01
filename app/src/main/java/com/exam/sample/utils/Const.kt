package com.exam.sample.utils

import com.exam.sample.R
import com.exam.sample.ui.fragment.FavoriteFragment
import com.exam.sample.ui.fragment.SearchFragment
import com.exam.sample.utils.extention.getStringColorHex

class Const {
    companion object {
        const val LOG_TAG = "GIPHY_TASK"
        const val API_KEY = "TXdYkIoK90rIz9Mz3QAIKsKgHyO073Vn"
        const val BASE_URL = "https://api.giphy.com/v1/"
        const val HTTP_CONNECT_TIMEOUT = 10L
        const val HTTP_WRITE_TIMEOUT = 1L
        const val HTTP_READ_TIMEOUT = 20L

        const val LIST_SPAN_COUNT = 3

        const val EXTRA_KEY_ACTIVITY_RESULT_DETAILACTIVITY_CLOSE_ADDARRAY = "extra_key_detailactivity_close_addarray"
        const val EXTRA_KEY_ACTIVITY_RESULT_DETAILACTIVITY_CLOSE_REMOVEARRAY = "extra_key_detailactivity_close_removearray"

        const val EXTRA_KEY_ACTIVITY_RESULT_DETAILACTIVITY_CLOSE = "extra_key_detailactivity_close"

        const val EXTRA_KEY_INTERACTION = "INTERACTION"
        const val LIMIT = 15 // The maximum number of objects to return. (Default: “25”)
        const val OFFSET_DEFAULT = 0 // Specifies the starting position of the results. Defaults to 0.
        const val EMIT_INTERVAL = 1000L
        const val BACKPRESS_TIME = 2000
        val TAB_TITLES = arrayListOf(
            R.string.tabNameTrending, R.string.tabNameArtists,
            R.string.tabNameClips
        )

        const val DB_NAME = "GiphyFavoriteDB.db"
        const val DB_SELECT = "SELECT"
        const val DB_INSERT = "INSERT"
        const val DB_DELETE = "DELETE"
        const val DB_UPDATE = "UPDATE"

        const val RX_EVENT_REFRESH_FAVORITE = "RxEventRefreshFavorite"

        const val USE_FOREGROUND = false
        const val WORKER_TAG = "WORKER_CHANGE_DATA"
        const val NOTI_ID = 1
        const val NOTIFICATION_CHANNEL_ID = "10001"
        const val NOTIFICATION_CHANNEL_NAME = "giphy_new_data"
        const val DATA_CHANGE_CHECKING_INTERVAL = 20 * 1000.toLong() // 10 seconds

        const val BTN_EVENT_SEND = 1
        const val BTN_EVENT_SHARE = 2

        val COLORS_RAINBOW = arrayListOf<Int>(
            R.color.colorRed, R.color.colorOrange, R.color.colorYellow,
            R.color.colorGreen, R.color.colorBlue, R.color.colorIndigo, R.color.colorPurple
        )

        val COLORS_RAINBOW_STRING = arrayListOf<String>(
            getStringColorHex(R.color.colorRed),
            getStringColorHex(R.color.colorOrange),
            getStringColorHex(R.color.colorYellow),
            getStringColorHex(R.color.colorGreen),
            getStringColorHex(R.color.colorBlue),
            getStringColorHex(R.color.colorIndigo),
            getStringColorHex(R.color.colorPurple)
        )


        var SCREEN_WIDTH = 0F
        var SCREEN_WIDTH_HALF = 0F

        const val HTTP_LOG = true
    }
}
