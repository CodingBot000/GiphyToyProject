package com.exam.sample.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import com.exam.sample.R
import com.exam.sample.adapter.ScreenSlideViewPagerAdapter
import com.exam.sample.databinding.ActivityMainBinding
import com.exam.sample.ui.base.BaseActivity
import com.exam.sample.ui.fragment.*
import com.exam.sample.ui.state.UIState
import com.exam.sample.utils.Const
import com.exam.sample.utils.animation.ZoomOutPageTransformer
import com.exam.sample.utils.delayOnLifecycle
import com.exam.sample.utils.toastMsg
import com.exam.sample.viewmodel.MainSharedViewModel
import com.exam.sample.viewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override val TAG: String
        get() = this.javaClass.name
    override val layoutResID: Int
        get() = R.layout.activity_main

    private val sharedViewModel by viewModels<MainSharedViewModel>()
//    private val viewModel by viewModels<MainViewModel>()
    private val viewPagerAdapter: ScreenSlideViewPagerAdapter by lazy { ScreenSlideViewPagerAdapter(this) }

    private val trendingFragment by lazy { TrendingFragment() }
    private val artistsFragment by lazy { ArtistsFragment() }
    private val clipsFragment by lazy { ClipsFragment() }

    private val fabBtnArray by lazy {
        arrayOf<FloatingActionButton>(binding.rlFabbtn.fabMain, binding.rlFabbtn.fabHome, binding.rlFabbtn.fabSearch, binding.rlFabbtn.fabFavorite)
    }
    private val fabOpen by lazy { AnimationUtils.loadAnimation(this,  R.anim.fab_open) }
    private val fabClose by lazy { AnimationUtils.loadAnimation(this, R.anim.fab_close) }
    private var isFabOpen = false

    private var backKeyPressedTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.v(Const.LOG_TAG, "$TAG onNewIntent")
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun init() {
        getScreenSize()
        fabInit()

        viewPagerAdapter.apply {
            addFragment(trendingFragment)
            addFragment(artistsFragment)
            addFragment(clipsFragment)
        }

        binding.apply {
//        pager.setPageTransformer(DepthPageTransformer())
            pager.setPageTransformer(ZoomOutPageTransformer())
            pager.adapter = viewPagerAdapter

            TabLayoutMediator(tabLayout, pager) { tab, position ->
                tab.text = getString(Const.TAB_TITLES[position])
                pager.setCurrentItem(tab.position, true)
            }.attach()
//
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    pager.currentItem = tab.position
                    hideBottomSheet()
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {
                }
            })
        }
    }

    override fun initObserver() {
        sharedViewModel.uiStateToActivity.asLiveData().observe(this@MainActivity, androidx.lifecycle.Observer {
            Log.v(TAG,"$TAG sharedViewModel uiStateToActivity event :$it")
            when (it) {
                UIState.INIT -> {
                }
                UIState.INIT_UI_MAINACT -> {
                    if (isFabOpen)
                        toggleFab()
                }
                UIState.REQ_REFRESH_DBLIST_FAVORITE -> {
                    if (isBottomSheetShow() && getCurrentBottomSheetFragment() is FavoriteFragment) {
                        sharedViewModel.notiFavoriteDBListRefreshEventToFavoriteView()
                    }
                } else -> {

                }
            }
        })
    }

    private fun fabInit() {
        binding.rlFabbtn.fabMain.setImageResource(R.drawable.musk_normal)
        fabBtnArray.forEach { fabBtn ->
            onFabClickListener.also {
                fabBtn.setOnClickListener(it)
            }
        }
    }

    private fun toggleFab() {
        if (isFabOpen)
            fabOpen()
        else
            fabClose()
        isFabOpen = !isFabOpen
    }

    private fun fabOpen() {
        binding.rlFabbtn.fabMain.setImageResource(R.drawable.musk_normal)

        fabBtnArray
            .filter { it !=binding.rlFabbtn.fabMain }
            .map { fab ->
                fab.startAnimation(fabClose)
                fab.isClickable = false
            }
    }

    private fun fabClose() {
        binding.rlFabbtn.fabMain.setImageResource(R.drawable.musk_smile)
        fabBtnArray
            .filter { it != binding.rlFabbtn.fabMain }
            .map { fab ->
                fab.startAnimation(fabOpen)
                fab.isClickable = true
            }
    }

    private val onFabClickListener = View.OnClickListener {
        toggleFab()

        when (it.id) {
            R.id.fabMain -> { }
            R.id.fabHome -> {
                binding.tabLayout.getTabAt(0)?.select()
            }
            R.id.fabSearch -> {
                val searchFragment = SearchFragment(closeEvent = { closeSearchFragment() })
                addFragmentToBottomSheet(searchFragment, searchFragment::class.java.simpleName)
            }
            R.id.fabFavorite -> {
                val favoriteFragment = FavoriteFragment(closeEvent = { closeFavoriteFragment() })
                addFragmentToBottomSheet(favoriteFragment, favoriteFragment::class.java.simpleName)
            }
        }
    }

    private fun closeSearchFragment() = hideBottomSheet()
    private fun closeFavoriteFragment() = hideBottomSheet()

    private fun addFragmentToBottomSheet(fragment: Fragment, fragmentTag: String) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_bottom_frag,
                R.anim.slide_out_top_frag,
                R.anim.slide_in_top_frag,
                R.anim.slide_out_bottom_frag
            )
            .replace(R.id.fl_fragmentcontainerview, fragment, fragmentTag)
            .addToBackStack(null)
            .commit()
    }


    private fun hideBottomSheet() =
        supportFragmentManager.popBackStack()


    private fun isBottomSheetShow(): Boolean =
         getCurrentBottomSheetFragment() != null


    private fun getCurrentBottomSheetFragment(): Fragment? =
         supportFragmentManager.findFragmentById(R.id.fl_fragmentcontainerview)


    private fun getScreenSize() {
        val metrics = resources.displayMetrics
        val screenWidth = metrics.widthPixels
        Const.SCREEN_WIDTH = screenWidth.toFloat()
        Const.SCREEN_WIDTH_HALF = (screenWidth / 2).toFloat()
    }

    override fun onBackPressed() {
        if (isBottomSheetShow()) {
            hideBottomSheet()
            return
        }

        if (System.currentTimeMillis() > backKeyPressedTime + Const.BACKPRESS_TIME) {
            backKeyPressedTime = System.currentTimeMillis()
            toastMsg(R.string.onemoretime)
            return
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + Const.BACKPRESS_TIME) {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
