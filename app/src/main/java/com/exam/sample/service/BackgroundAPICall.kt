package com.exam.sample.service


import android.util.Log
import com.exam.sample.domain.usecase.UseCaseGetTrendingData
import com.exam.sample.model.data.TrendingData
import com.exam.sample.utils.Const
import com.exam.sample.utils.isNetworkConnected


class BackgroundAPICall
{
    companion object {
        @Volatile private var instance: BackgroundAPICall? = null

        @JvmStatic fun getInstance(): BackgroundAPICall =
            instance ?: synchronized(this) {
                instance ?: BackgroundAPICall().also {
                    instance = it
                }
            }
    }

    interface ServiceListener {
        fun listenerFromService(data: TrendingData)
    }

    var useCaseGetTrendingData: UseCaseGetTrendingData? = null
    var serviceListener: ServiceListener? = null


    fun getDataFromBackground(offset: Int, rating: String = "") {
        if (!isNetworkConnected())
            return

        useCaseGetTrendingData?.let{
            it.setData(offset, rating)
            it.execute(
                onSuccess = {
                    serviceListener?.listenerFromService(it)
                },
                onError = {
                },
                onFinished = {
                }
            )
        }
    }
}
