package com.rootscare.serviceprovider.utilitycommon

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util
import java.lang.ref.WeakReference

class MyExoPlayer(mPW: PlayerView, mContext: Context, private val mPlayUrl: String) :
    Player.EventListener {

    private var simpleExoplayer: SimpleExoPlayer? = null
    private var currentWindow = 0
    private val mCtx: WeakReference<Context> = WeakReference(mContext)
    private val mPlayerView: WeakReference<PlayerView> = WeakReference(mPW)

    init {
        simpleExoplayer = mCtx.get()?.let { SimpleExoPlayer.Builder(it).build() }
        mPlayerView.get()?.player = simpleExoplayer
        mPlayerView.get()?.setKeepContentOnPlayerReset(true)
    }

    private fun initializePlayer() {

        // set the play url to play the audio
        val mediaItem: MediaItem = MediaItem.fromUri(mPlayUrl)

        simpleExoplayer?.run {
            setMediaItem(mediaItem)
            seekTo(0)
            playWhenReady = false
            addListener(this@MyExoPlayer)
            prepare()
        }

    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        val stateString: String
        when (playbackState) {
            ExoPlayer.STATE_IDLE -> stateString = "ExoPlayer.STATE_IDLE      -"
            ExoPlayer.STATE_BUFFERING -> stateString = "ExoPlayer.STATE_BUFFERING -"
            ExoPlayer.STATE_READY -> stateString = "ExoPlayer.STATE_READY     -"
            ExoPlayer.STATE_ENDED -> {
                stateString = "ExoPlayer.STATE_ENDED     -"
                simpleExoplayer?.seekTo(0)
                simpleExoplayer?.playWhenReady = false
            }
            else -> stateString = "UNKNOWN_STATE             -"
        }
        Log.d("Exo_Player", "changed state to $stateString")
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        android.util.Log.e("Exo_Player", "onIsPlayingChanged: $isPlaying")
    }

    internal fun onStart() {
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    internal fun onResume() {
        if (Util.SDK_INT < 24 || simpleExoplayer == null) {
            initializePlayer()
        }
    }

    internal fun onPause() {
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    internal fun onStop() {
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }

    }

    private fun releasePlayer() {
        if (simpleExoplayer != null) {
            currentWindow = simpleExoplayer?.currentWindowIndex ?: 0
            simpleExoplayer?.removeListener(this@MyExoPlayer)
            simpleExoplayer?.release()
            simpleExoplayer = null
        }
    }
}