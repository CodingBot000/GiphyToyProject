package com.exam.sample

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner


import androidx.work.*
import com.exam.sample.common.ApplicationLifeCycleListener

import com.exam.sample.service.ServiceActions
import com.exam.sample.service.ServiceDataChangeObserver
import com.exam.sample.service.WorkerDataChangeMonitor
import com.exam.sample.utils.Const
import com.exam.sample.utils.isServiceRunning
import com.facebook.stetho.Stetho
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class App : Application() {
    companion object {
        private var sApplication: Application? = null
        private var sApplicationLifecycle: Lifecycle.Event? = null

        fun getApplication(): Application {
            return sApplication!!
        }

        fun getLifeCycleApplication(): Lifecycle.Event {
            return sApplicationLifecycle ?: Lifecycle.Event.ON_START
        }
    }

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        sApplication = this

        val serviceIntent by lazy {
            Intent(this, ServiceDataChangeObserver::class.java)
        }

        ProcessLifecycleOwner.get().lifecycle
            .addObserver(
                ApplicationLifeCycleListener(object : ApplicationLifeCycleListener.LifeCycleListenerCallback {
                    override fun lifeCycleCallback(event: Lifecycle.Event) {

                        sApplicationLifecycle = event
                        Log.v(Const.LOG_TAG, "LifeCycleListener ProcessLifecycleOwner event=> $event")
                        Log.v(Const.LOG_TAG, "LifeCycleListener ProcessLifecycleOwner sApplicationLifecycle=> $sApplicationLifecycle")
                        if (event == Lifecycle.Event.ON_START) {
                            if (Const.USE_FOREGROUND) {
                                // App이 Foreground상태일때는 서비스를 동작하지않는다
                                if (isServiceRunning(ServiceDataChangeObserver::class.java))
                                    stopService(serviceIntent)

                                // 올라온 노티를 제거한다.
                                val notificationManager =
                                    applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                                notificationManager.cancel(Const.NOTI_ID)

                            } else {
                                WorkManager.getInstance()?.cancelAllWork()
                            }
                        } else if (event == Lifecycle.Event.ON_STOP) {
                            if (Const.USE_FOREGROUND) {

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    serviceIntent.action = ServiceActions.START_FOREGROUND
                                    if (!isServiceRunning(ServiceDataChangeObserver::class.java))
                                        startForegroundService(serviceIntent)
                                } else {
                                    startService(serviceIntent)
                                }
                            } else {
//                                doWorkPeriodic()
                            }
                        }
                    }
                })
            )
    }

    fun doWorkPeriodic() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<WorkerDataChangeMonitor>(15, TimeUnit.MINUTES).build()
        PeriodicWorkRequest.Builder(WorkerDataChangeMonitor::class.java, PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MINUTES)
            .setConstraints(constraints).build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(Const.WORKER_TAG, ExistingPeriodicWorkPolicy.REPLACE, workRequest!!)
    }
}
