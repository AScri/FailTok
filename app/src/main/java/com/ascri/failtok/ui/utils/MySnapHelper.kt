package com.ascri.failtok.ui.utils

import android.view.View
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.ascri.failtok.ui.adapters.FailItem

class MySnapHelper(private val recyclerView: RecyclerView) : PagerSnapHelper(){
    private var previousSnapTarget = 0

    init {
        this.attachToRecyclerView(recyclerView)
    }

    override fun createSnapScroller(layoutManager: RecyclerView.LayoutManager?): LinearSmoothScroller? {
        return super.createSnapScroller(layoutManager)
    }

    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        super.attachToRecyclerView(recyclerView)
    }

    override fun onFling(velocityX: Int, velocityY: Int): Boolean {
        return super.onFling(velocityX, velocityY)
    }

    override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View): IntArray? {
        return super.calculateDistanceToFinalSnap(layoutManager, targetView)
    }

    override fun findTargetSnapPosition(
        layoutManager: RecyclerView.LayoutManager?,
        velocityX: Int,
        velocityY: Int
    ): Int {
        val position = super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
        (recyclerView.findViewHolderForAdapterPosition(previousSnapTarget) as? FailItem.FailViewHolder)?.onItemInvisible()
        (recyclerView.findViewHolderForAdapterPosition(position) as? FailItem.FailViewHolder)?.onItemVisible()
        previousSnapTarget = position
        return position
    }

    override fun calculateScrollDistance(velocityX: Int, velocityY: Int): IntArray {
        return super.calculateScrollDistance(velocityX, velocityY)
    }

    override fun createScroller(layoutManager: RecyclerView.LayoutManager?): RecyclerView.SmoothScroller? {
        return super.createScroller(layoutManager)
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager?): View? {
        return super.findSnapView(layoutManager)
    }
}