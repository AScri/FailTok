package com.ascri.failtok.utilities.extensions

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ImageView
import com.ascri.failtok.FailTokApp.Companion.TAG
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class MyExoPlayer(context: Context) {
    private val dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "livestream-fails"))
    private val trackSelector = DefaultTrackSelector(AdaptiveTrackSelection.Factory(DefaultBandwidthMeter()))
    var player: SimpleExoPlayer? = null
    var thumbnailView: ImageView? = null
    var playerView: PlayerView? = null

    init {
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
        player?.addListener(object : Player.DefaultEventListener() {

            override fun onPlayerError(error: ExoPlaybackException?) {
                Log.w("LOG", "onPlayerError: Cannot play video")
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_IDLE -> {
                        thumbnailView?.visibility = VISIBLE
                        playerView?.visibility = INVISIBLE
                    }
                    Player.STATE_READY -> {
                        if (playWhenReady) {
                            thumbnailView?.animate()
                                ?.alpha(0F)
                                ?.setDuration(250)
                                ?.withStartAction {
                                    playerView?.visibility = VISIBLE
                                    Log.d(TAG, "onPlayerStateChanged: start")
                                }
                                ?.withEndAction {
                                    thumbnailView?.visibility = INVISIBLE
                                    thumbnailView?.alpha = 1F
                                    Log.d(TAG, "onPlayerStateChanged: end")
                                }
                        }
                    }
                }
            }
        })
        player?.repeatMode = Player.REPEAT_MODE_ALL
    }

    fun prepareVideoPlayer(videoUrl: String, thumbnailView: ImageView, playerView: PlayerView) {
        this.thumbnailView = thumbnailView
        this.playerView = playerView
        this.playerView?.setControllerVisibilityListener {
            when (it) {
                PlayerControlView.VISIBLE -> {
                    player?.playWhenReady = false
                    this.playerView?.showController()
                }
                PlayerControlView.GONE -> {
                    player?.playWhenReady = true
                    this.playerView?.hideController()
                }
            }
        }
        val videoSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(videoUrl))
        player?.prepare(videoSource)
        player?.seekTo(0, 0)
        player?.playWhenReady = true
        playerView.player = player
    }


}