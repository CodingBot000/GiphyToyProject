package com.exam.sample.service

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import com.exam.sample.App
import com.exam.sample.R
import com.exam.sample.model.data.TrendingData
import com.exam.sample.utils.Const
import com.exam.sample.utils.toastMsg
import com.exam.sample.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ServiceDataChangeObserver : Service() {

    private var dataOld: TrendingData? = null
    private var mTimer: Timer? = null
    private var debugCnt = 0

    override fun onCreate() {
        super.onCreate()
        Log.e(Const.LOG_TAG, "onCreate()")

        mTimer?.cancel()
        if (mTimer == null)
            mTimer = Timer()

        mTimer?.scheduleAtFixedRate(TimeDisplayTimerTask(), 0, Const.DATA_CHANGE_CHECKING_INTERVAL)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(Const.LOG_TAG, "ServiceAction Received = ${intent?.action}")

        when (intent?.action) {
            ServiceActions.START_FOREGROUND -> {
                startForegroundService()
            }
            ServiceActions.STOP_FOREGROUND -> {
                stopForegroundService()
            }
        }

//        viewModel.serviceListener = object : MainViewModel.ServiceListener {
//            override fun listenerFromService(data: TrendingData) {
//                Log.d(Const.LOG_TAG, "Service listenerFromService")
//                // 데이터를 정기적으로 받아온다.
//                // 받아온 데이터가 이전데이터와 다르다면 데이터가 변경되었으므로 푸시로 알린다.
//                // 변경 알림을 통보한 후  더이상 서비스를 동작시키지않는다.
//                dataOld?.let {
//                    if ((dataOld as TrendingData).trendingItems[0].embed_url == data.trendingItems[0].embed_url) {
//                        Log.v(Const.LOG_TAG, "noti msg equals $debugCnt")
//                    } else {
//                        Log.v(Const.LOG_TAG, "noti msg NOT equals $debugCnt")
//
//                        stopSelf()
//                        DataChangeNotification.updateMessage(
//                            App.getApplication().getString(R.string.newDataNotiMessage)
//                        )
//                    }
//                    debugCnt++
//                }
//                dataOld = data
//            }
//        }
        return START_STICKY
    }


    override fun onDestroy() {
        mTimer?.cancel()
        mTimer = null
        super.onDestroy()
        Log.e(Const.LOG_TAG, "onDestroy()")
    }

    private fun startForegroundService() {
        Log.d(Const.LOG_TAG, "Service startForegroundService")
        val notiBuild = DataChangeNotification.createNotification(
            this, App.getApplication().getString(R.string.initialNotiMessage)
        )
        startForeground(Const.NOTI_ID, notiBuild.build())

    }

    private fun stopForegroundService() {
        Log.d(Const.LOG_TAG, "Service stopForegroundService")
        stopForeground(true)
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    inner class TimeDisplayTimerTask : TimerTask() {
        private fun getDateTime(): String {
            val sdf = SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]")
            return sdf.format(Date())
        }

        override fun run() {
//            BackgroundAPICall.getInstance().getDataFromBackground(0)
            toastMsg("request API for searching change data in Background ${getDateTime()}")
            Log.v(Const.LOG_TAG, "getDateTime()  ${getDateTime()}")
        }
    }
}
