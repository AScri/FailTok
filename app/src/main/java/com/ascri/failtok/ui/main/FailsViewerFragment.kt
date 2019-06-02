package com.ascri.failtok.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ascri.failtok.FailTokApp
import com.ascri.failtok.FailTokApp.Companion.TAG
import com.ascri.failtok.R
import com.ascri.failtok.databinding.FailsViewerFragmentBinding
import com.ascri.failtok.ui.adapters.FailItem
import com.ascri.failtok.ui.adapters.ProgressItem
import com.ascri.failtok.ui.utils.MySnapHelper
import com.ascri.failtok.ui.viewModels.FailsViewModel
import com.ascri.failtok.utilities.extensions.MyExoPlayer
import dagger.android.support.DaggerFragment
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

open class FailsViewerFragment : DaggerFragment(), FlexibleAdapter.EndlessScrollListener{
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var binding: FailsViewerFragmentBinding
    val compositeDisposable = CompositeDisposable()
    val adapter = FlexibleAdapter<IFlexible<*>>(mutableListOf(), this, true)
    lateinit var myPlayer: MyExoPlayer
    lateinit var type: FailsViewModel.FailsType

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fails_viewer_fragment, container, false)
        binding.viewModel = ViewModelProviders.of(this, viewModelFactory).get(FailsViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner

        initRecyclerView()

        myPlayer = context?.let { MyExoPlayer(it) } ?: MyExoPlayer(FailTokApp.appComponent.getAppContext())
        binding.viewModel?.init(type)
        binding.viewModel?.getPage()?.observe(
            viewLifecycleOwner,
            Observer {
                if (adapter.itemCount == 0) {
                    adapter.updateDataSet(it.map { wall -> FailItem(wall, binding.viewModel!!, myPlayer) })
                } else adapter.onLoadMoreComplete(it.map { wall -> FailItem(wall, binding.viewModel!!, myPlayer) })
            })
        return binding.root
    }

    private fun initRecyclerView() {
        adapter.setEndlessScrollListener(this, ProgressItem())
            .setEndlessScrollThreshold(3)
            .endlessPageSize = 20
        binding.recyclerView.adapter = this.adapter
        binding.recyclerView.layoutManager = SmoothScrollLinearLayoutManager(context)
        binding.recyclerView.addItemDecoration(
            FlexibleItemDecoration(requireContext())
        )
        MySnapHelper(binding.recyclerView)
    }

    override fun noMoreLoad(newItemsSize: Int) {
        Log.d(TAG, "noMoreLoad: newItemsSize=$newItemsSize")
        Log.d(TAG, "noMoreLoad: Total pages loaded=${adapter.endlessCurrentPage}")
        Log.d(TAG, "noMoreLoad: Total items loaded=${adapter.mainItemCount}")
    }

    override fun onLoadMore(lastPosition: Int, currentPage: Int) {
        Log.d(TAG, "onLoadMore: lastPosition = $lastPosition, currentPage: $currentPage")
        binding.viewModel?.loadByType(type, currentPage + 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        myPlayer.player?.release()
        compositeDisposable.dispose()
    }

    override fun onStop() {
        super.onStop()
        myPlayer.player?.playWhenReady = false
    }

    override fun onStart() {
        super.onStart()
        myPlayer.player?.playWhenReady = true
    }
}