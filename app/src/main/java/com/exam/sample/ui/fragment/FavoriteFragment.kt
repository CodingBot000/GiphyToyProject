package com.exam.sample.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.room.EmptyResultSetException
import com.exam.sample.R
import com.exam.sample.adapter.StaggeredAdapter
import com.exam.sample.databinding.FragmentFavoriteBinding
import com.exam.sample.livedata.EventObserver
import com.exam.sample.model.data.FavoriteInfo
import com.exam.sample.ui.DetailActivity
import com.exam.sample.ui.base.BaseFragment
import com.exam.sample.utils.Const
import com.exam.sample.utils.Status
import com.exam.sample.utils.extention.alertDialog
import com.exam.sample.utils.extention.startActivityDetailExtras
import com.exam.sample.utils.extention.touchEffect
import com.exam.sample.utils.snackBarSimpleAlert
import com.exam.sample.utils.toastMsg
import com.exam.sample.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment constructor(private val closeEvent: () -> Unit) : BaseFragment<FragmentFavoriteBinding, FavoriteViewModel>() {

    override val TAG: String
        get() = this.javaClass.name
    override val layoutResID: Int
        get() = R.layout.fragment_favorite

    private val viewModel by viewModels<FavoriteViewModel>()
    private val adapterRecycler: StaggeredAdapter by lazy {
        StaggeredAdapter(itemListClick = { item, gridBindingItem ->
            gridBindingItem.img.touchEffect(requireActivity())
            requireActivity().startActivityDetailExtras(DetailActivity::class.java, item)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }


    override fun init() {
        if (!adapterRecycler.hasObservers())
            adapterRecycler.setHasStableIds(true)

        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            itemAnimator = null
            adapter = adapterRecycler
        }

        onClickListener()
    }

    override fun initObserver() {
        viewModel.apply {

            isLoading.observe(
                requireActivity(),
                EventObserver {
                    binding.progress.isVisible = it
                }
            )

            dbEvent.observe(
                requireActivity(),
                EventObserver {

                    when (it.status) {
                        Status.SUCCESS -> {
                            if (it.data?.flag == Const.DB_DELETE) {
                                toastMsg(getString(R.string.favorite_item_delete_all_msg, it.data.data as Int))
                                adapterRecycler.clearItem()
                            } else {
                                val list = it.data?.data as List<FavoriteInfo>
                                if (list.isNotEmpty())
                                    viewModel.getFavoriteInfoRequest(list)
                                else
                                    snackBarSimpleAlert(
                                        R.string.noFavorites,
                                        R.string.ok,
                                        binding.toolbar
                                    )
                            }
                        }

                        Status.ERROR -> {
                            if (it.data?.data is EmptyResultSetException) {
                                Log.v(Const.LOG_TAG, "Query returned Empty")
                            } else {
                                toastMsg(it.message ?: "")
                            }
                        }
                    }
                }
            )

            itemLiveData.observe(
                requireActivity(),
                EventObserver {
                    when (it.status) {
                        Status.SUCCESS -> {
                            it.data?.let { data -> adapterRecycler.initItem(data.trendingItems) }
                        }

                        Status.ERROR -> {
                            toastMsg(it.message ?: "")
                        }
                    }
                }
            )
        }
    }

    private fun initData() {
        viewModel.apply {
            getFavoriteAll()
        }
    }
    private fun onClickListener() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                closeEvent()
            }
            fabDown.setOnClickListener {
                closeEvent()
            }
            btnRemoveAll.setOnClickListener {
                requireActivity().alertDialog(
                    msg = getString(R.string.favorite_item_delete_all),
                    onClickOK = {
                        this@FavoriteFragment.viewModel.removeAllFavoriteDB()
                    },
                    onClickCancel = {

                    }
                )

            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
    }
}
