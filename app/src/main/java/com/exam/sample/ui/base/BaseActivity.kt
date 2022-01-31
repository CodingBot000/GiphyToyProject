package com.exam.sample.ui.base

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.exam.sample.viewmodel.base.BaseViewModel

abstract class BaseActivity<B : ViewDataBinding?, VM : BaseViewModel>() : AppCompatActivity() {
    var _binding: B? = null
    val binding: B get() = _binding!!

//    abstract val viewModel: VM
    abstract val layoutResID: Int
    abstract val TAG: String

    abstract fun init()
    abstract fun initObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                statusBarColor = Color.TRANSPARENT
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
        super.onCreate(savedInstanceState)

        _binding = DataBindingUtil.setContentView(this, layoutResID)
        _binding?.lifecycleOwner = this

        init()
        initObserver()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
