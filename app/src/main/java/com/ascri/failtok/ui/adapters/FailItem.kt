package com.ascri.failtok.ui.adapters

import android.content.Intent
import android.net.Uri
import android.view.View
import com.ascri.failtok.R
import com.ascri.failtok.data.models.Fail
import com.ascri.failtok.databinding.RvItemFailBinding
import com.ascri.failtok.ui.viewModels.FailsViewModel
import com.ascri.failtok.utilities.extensions.MyExoPlayer
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

class FailItem(private val fail: Fail, val viewModel: FailsViewModel, val myExoPlayer: MyExoPlayer) :
        AbstractFlexibleItem<FailItem.FailViewHolder>() {

    override fun equals(other: Any?): Boolean {
        return if (other is Fail) {
            fail == other
        } else false
    }

    override fun hashCode(): Int {
        return fail.hashCode()
    }

    override fun getLayoutRes(): Int = R.layout.rv_item_fail

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<*>>): FailViewHolder =
            FailViewHolder(RvItemFailBinding.bind(view), adapter)

    override fun bindViewHolder(
            adapter: FlexibleAdapter<IFlexible<*>>,
            holder: FailViewHolder,
            position: Int,
            payloads: List<Any>
    ) {
        holder.bind(fail, position)
        if (position == 0) holder.onItemVisible()
    }

    inner class FailViewHolder(val binding: RvItemFailBinding, adapter: FlexibleAdapter<*>) :
            FlexibleViewHolder(binding.root.rootView, adapter), ItemVisibilityStateListener {

        override fun onItemVisible() {
            binding.fail?.postId?.let {
                viewModel.fetchDetails(it) { fetchedFail ->
                    myExoPlayer.prepareVideoPlayer(fetchedFail.videoUrl, binding.thumbnailImage, binding.playerView)
                    binding.sourceLink.isEnabled = true
                    binding.sourceLink.setOnClickListener {
                        binding.sourceLink.context.startActivity(
                                Intent(Intent.ACTION_VIEW).setData(Uri.parse(fetchedFail.sourceUrl))
                        )
                    }
                }
            }
        }

        override fun onItemInvisible() {
            binding.thumbnailImage.visibility = View.VISIBLE
            binding.thumbnailImage.alpha = 1F
            binding.playerView.visibility = View.INVISIBLE
            binding.playerView.hideController()
            binding.playerView.player = null
            fail.points.toString()
        }

        fun bind(fail: Fail, position: Int) {
            with(binding) {
                this.fail = fail
                this.position = position
                executePendingBindings()
            }

        }
    }

    interface ItemVisibilityStateListener {
        fun onItemVisible()
        fun onItemInvisible()
    }
}