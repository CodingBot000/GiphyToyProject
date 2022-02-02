package com.exam.sample.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.exam.sample.App
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.*


fun shareUrl(shareUrl: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareUrl)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    shareIntent.flags = FLAG_ACTIVITY_NEW_TASK
    App.getApplication().startActivity(shareIntent)
}


fun checkIsMaterialVersion() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

fun toastMsg(resId: Int) {
    toastMsg(App.getApplication().getString(resId))
}

@SuppressLint("CheckResult")
fun toastMsg(msg: String) {
    if (msg.isEmpty())
        return

    Maybe.just(0).observeOn(AndroidSchedulers.mainThread()).subscribe {
        Toast.makeText(App.getApplication(), msg, Toast.LENGTH_SHORT).show()
    }
}

fun isNetworkConnected(): Boolean {
    val cm = App.getApplication().applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    val isConnected = activeNetwork != null && activeNetwork.isConnected
    return isConnected
}

fun snackBarSimpleAlert(msgResId: Int, btnNameResId: Int, view: View) {
    val snackbar = Snackbar.make(
        view,
        msgResId,
        Snackbar.LENGTH_SHORT
    )
    snackbar.setAction(App.getApplication().applicationContext.getString(btnNameResId)) {
        snackbar.dismiss()
    }
    snackbar.show()
}

fun View.delayOnLifecycle(
    durationInMillis: Long,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: () -> Unit
) : Job? = findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
    lifecycleOwner.lifecycle.coroutineScope.launch(dispatcher) {
        delay(durationInMillis)
        block()
    }
}
