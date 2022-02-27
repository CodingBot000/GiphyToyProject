package com.exam.sample


import android.app.Application
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import com.exam.sample.common.ApplicationLifeCycleListener
import com.exam.sample.utils.Const
import dagger.hilt.android.HiltAndroidApp

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
        sApplication = this

        ProcessLifecycleOwner.get().lifecycle
            .addObserver(
                ApplicationLifeCycleListener(object : ApplicationLifeCycleListener.LifeCycleListenerCallback {
                    override fun lifeCycleCallback(event: Lifecycle.Event) {

                        sApplicationLifecycle = event
                        Log.v(Const.LOG_TAG, "LifeCycleListener ProcessLifecycleOwner event=> $event")
                        Log.v(Const.LOG_TAG, "LifeCycleListener ProcessLifecycleOwner sApplicationLifecycle=> $sApplicationLifecycle")

                    }
                })
            )
    }

}
