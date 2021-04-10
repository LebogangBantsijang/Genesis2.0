/*
 * Copyright (c) 2021. - Lebogang Bantsijang
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.lebogang.genesis.ui

import android.annotation.SuppressLint
import android.os.Build
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.lebogang.genesis.R
import com.lebogang.genesis.databinding.ActivityMainBinding
import com.lebogang.genesis.service.MusicService
import com.lebogang.genesis.service.Queue
import com.lebogang.genesis.interfaces.OnStateChangedListener
import com.lebogang.genesis.interfaces.PlaybackState
import com.lebogang.genesis.interfaces.RepeatSate
import com.lebogang.genesis.settings.PlayerBackgroundType
import com.lebogang.genesis.settings.PlayerSettings
import com.lebogang.genesis.ui.helpers.BottomSheetHelper
import com.lebogang.genesis.ui.helpers.SeekBarHelper
import com.lebogang.genesis.utils.SeekBarThreader
import com.lebogang.genesis.utils.glide.GlideManager

@SuppressLint("ClickableViewAccessibility")
class Player(private val activity:MainActivity, private val viewBinding: ActivityMainBinding):
    BottomSheetHelper(), OnStateChangedListener{
    private val bottomSheet = BottomSheetBehavior.from(viewBinding.player.bottomSheet).apply {
        state = BottomSheetBehavior.STATE_HIDDEN
        addBottomSheetCallback(this@Player) }
    private val gestureDetector = GestureDetectorCompat(activity, object : GestureDetector.SimpleOnGestureListener(){
        override fun onDoubleTap(e: MotionEvent?): Boolean {
            musicService.togglePlayPause()
            return true
        } })
    private val playerSettings = PlayerSettings(activity)
    lateinit var musicService: MusicService
    lateinit var seekBarThreader:SeekBarThreader

    init {
        initOnClicks()
        Queue.currentAudio.observe(activity, {
            //launcher
            if (viewBinding.launcherView.root.visibility == View.GONE)
                viewBinding.launcherView.root.visibility = View.VISIBLE
            viewBinding.launcherView.titleView.text = it.title
            viewBinding.launcherView.subtitleView.text = it.artist
            GlideManager(activity).loadAudioArt(it.getArtUri(), viewBinding.launcherView.imageView)
            //player
            viewBinding.player.titleView.text = it.title
            viewBinding.player.subtitleView.text = it.artist
            viewBinding.player.thirdTitle.text = it.album
            viewBinding.player.seekBar.max = it.duration.toInt()
            viewBinding.player.durationView.text = it.durationFormatted
            GlideManager(activity).loadAudioArtCircularCrop(it.getArtUri(), viewBinding.player.imageView)
            changeBackground()
        })
        if (playerSettings.getBackgroundType() == PlayerBackgroundType.GIF)
            loadGif()
    }

    private fun initOnClicks(){
        viewBinding.launcherView.root.setOnClickListener { showSheet() }
        viewBinding.player.volumeView.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                val controller = activity.findNavController(R.id.navFragmentContainer)
                controller.navigate(R.id.audioProcessingDialog)
            }else{
                Snackbar.make(viewBinding.root,"Not Supported", Snackbar.LENGTH_LONG).show()
            }
        }
        viewBinding.launcherView.queueView.setOnClickListener {
            val controller = activity.findNavController(R.id.navFragmentContainer)
            controller.navigate(R.id.queueDialog) }
        viewBinding.launcherView.playPauseView.setOnClickListener { musicService.togglePlayPause() }
        viewBinding.player.seekBar.setOnSeekBarChangeListener(object : SeekBarHelper(){
            override fun progressChanged(progress: Int) {
                musicService.seekTo(progress)
            }
        })
        viewBinding.player.previousView.setOnClickListener { musicService.skipToPrevious() }
        viewBinding.player.playPauseView.setOnClickListener { musicService.togglePlayPause() }
        viewBinding.player.nextView.setOnClickListener { musicService.skipToNext() }
        viewBinding.player.queueView.setOnClickListener {
            val controller = activity.findNavController(R.id.navFragmentContainer)
            controller.navigate(R.id.queueDialog) }
        viewBinding.player.shareView.setOnClickListener {
            //finish later
        }
        viewBinding.player.repeatView.setOnClickListener { musicService.changeRepeatMode() }
        viewBinding.player.backView.setOnClickListener { hideSheet() }
        viewBinding.player.gifView.setOnClickListener {
            val controller = activity.findNavController(R.id.navFragmentContainer)
            controller.navigate(R.id.playerBackgroundDialog)
        }
        // gestures
        viewBinding.player.artView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }
    }

    fun changeBackground(){
        val uri = Queue.currentAudio.value?.getArtUri()
        uri?.let {
            when(playerSettings.getBackgroundType()){
                PlayerBackgroundType.NONE -> viewBinding.player.artView.setImageBitmap(null)
                PlayerBackgroundType.ADAPTIVE_BLURRY ->
                    GlideManager(activity).loadBlurred(activity,it,viewBinding.player.artView)
                PlayerBackgroundType.ADAPTIVE_IMAGE ->
                    GlideManager(activity).loadAudioArt(it, viewBinding.player.artView)
                PlayerBackgroundType.GIF -> {
                    //do nothing
                }
            }
        }
    }

    fun loadGif(){
        GlideManager(activity).loadGif(activity,viewBinding.player.artView)
    }

    fun isSheetShowing():Boolean{
        if (bottomSheet.state == BottomSheetBehavior.STATE_EXPANDED){
            hideSheet()
            return true
        }
        return false
    }

    private fun hideSheet(){
        bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun showSheet(){
        bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onStateChanged(newState: Int) {
        if (newState == BottomSheetBehavior.STATE_HALF_EXPANDED)
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        else if (newState == BottomSheetBehavior.STATE_EXPANDED)
            viewBinding.player.artView.requestFocus()
        else
            viewBinding.player.artView.clearFocus()
    }

    override fun onPlaybackChanged(playbackState: PlaybackState) {
        if (playbackState == PlaybackState.PLAYING){
            viewBinding.launcherView.playPauseView.setImageResource(R.drawable.ic_pause)
            viewBinding.player.playPauseView.setImageResource(R.drawable.ic_pause)
        }
        else{
            viewBinding.launcherView.playPauseView.setImageResource(R.drawable.ic_play)
            viewBinding.player.playPauseView.setImageResource(R.drawable.ic_play)
        }
        seekBarThreader.onPlaybackStateChanged(playbackState)
    }

    fun getStateListener(): OnStateChangedListener {
        return this
    }

    override fun onRepeatModeChange(repeatSate: RepeatSate) {
        when(repeatSate){
            RepeatSate.REPEAT_NONE -> viewBinding.player.repeatView.setImageResource(R.drawable.ic_repeat_none)
            RepeatSate.REPEAT_ALL -> viewBinding.player.repeatView.setImageResource(R.drawable.ic_repeat)
            RepeatSate.REPEAT_ONE -> viewBinding.player.repeatView.setImageResource(R.drawable.ic_repeat_one)
            RepeatSate.SHUFFLE_ALL -> viewBinding.player.repeatView.setImageResource(R.drawable.ic_shuffle)
        }
    }

}
