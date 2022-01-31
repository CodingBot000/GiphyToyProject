package com.exam.sample.service

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.exam.sample.App
import com.exam.sample.R
import com.exam.sample.model.data.TrendingData
import com.exam.sample.utils.Const


class WorkerDataChangeMonitor (context: Context, workerParams: WorkerParameters) :
    Worker(
        context,
        workerParams
    ),
    BackgroundAPICall.ServiceListener {


//    lateinit var serviceAPICall: ServiceAPICall
    private var dataOld: TrendingData? = null

    private var debugCnt = 0

    override fun doWork(): Result {
        Log.d(Const.LOG_TAG, "WorkerDataChangeObserver doWork")
        BackgroundAPICall.getInstance().serviceListener = this
        BackgroundAPICall.getInstance().getDataFromBackground(0)

        return Result.success()
    }

    override fun listenerFromService(data: TrendingData) {
        Log.v(Const.LOG_TAG, "listenerFromService")

        dataOld?.let {
            if (dataOld!!.trendingItems[0].embed_url == data.trendingItems[0].embed_url) {
                Log.v(Const.LOG_TAG, "noti msg equals $debugCnt")
            } else {
                Log.v(Const.LOG_TAG, "noti msg NOT equals $debugCnt")

                if (Const.USE_FOREGROUND) {
                    DataChangeNotification.updateMessage(App.getApplication().getString(R.string.newDataNotiMessage))
                } else {
                    val notiBuilder = DataChangeNotification.createNotification(App.getApplication().applicationContext, App.getApplication().getString(R.string.newDataNotiMessage))
                    NotificationManagerCompat.from(App.getApplication().applicationContext)
                        .notify(Const.NOTI_ID, notiBuilder.build())
                }
            }
            debugCnt++
        }
        dataOld = data
    }
}
