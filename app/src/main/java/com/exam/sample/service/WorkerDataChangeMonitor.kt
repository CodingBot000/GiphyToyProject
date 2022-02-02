package com.exam.sample.service

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.exam.sample.App
import com.exam.sample.R
import com.exam.sample.domain.usecase.UseCaseGetTrendingData
import com.exam.sample.model.data.TrendingData
import com.exam.sample.utils.Const
import com.exam.sample.utils.isNetworkConnected

class WorkerDataChangeMonitor @WorkerInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private var useCaseGetTrendingData: UseCaseGetTrendingData
    )
    : Worker(context, workerParameters)
{
    private var dataOld: TrendingData? = null

    private var debugCnt = 0

    override fun doWork(): Result {
        Log.d(Const.LOG_TAG, "WorkerDataChangeObserver doWork")

        getDataFromBackground(0)
        return Result.success()
    }

    fun listenerFromService(data: TrendingData) {
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


    fun getDataFromBackground(offset: Int, rating: String = "") {
        if (!isNetworkConnected())
            return

        useCaseGetTrendingData?.let{
            it.setData(offset, rating)
            it.execute(
                onSuccess = { it ->
//                    serviceListener?.listenerFromService(it)
                    listenerFromService(it)
                },
                onError = {
                },
                onFinished = {
                }
            )
        }
    }
}
