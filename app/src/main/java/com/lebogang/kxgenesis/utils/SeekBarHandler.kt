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

package com.lebogang.kxgenesis.utils

import android.os.Handler
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.service.MusicService
import com.lebogang.kxgenesis.service.utils.PlaybackState
import com.lebogang.kxgenesis.ui.PlayerActivity

class SeekBarHandler(private val activity: PlayerActivity, private val musicService: MusicService)
    : Handler(activity.mainLooper){
    private val seekBar:SeekBar by lazy {
        activity.findViewById(R.id.seekBar)
    }
    private val timerView:TextView by lazy {
        activity.findViewById(R.id.timerView)
    }
    init {
        activity.lifecycle.addObserver(StateObserver())
    }
    private val runnable = Run()
    private var isHandlerRunning = false

    fun onPlaybackStateChanged(playbackState: PlaybackState){
        if (playbackState == PlaybackState.PLAYING){
            if (!isHandlerRunning){
                post(runnable)
            }
        } else{
            removeCallbacks(runnable)
            isHandlerRunning = false
        }
    }

    inner class StateObserver:DefaultLifecycleObserver{
        override fun onResume(owner: LifecycleOwner) {
            super.onResume(owner)
            if (musicService.getPlaybackState() == PlaybackState.PLAYING){
                if (!isHandlerRunning){
                    this@SeekBarHandler.post(runnable)
                }
            }
            else{
                this@SeekBarHandler.removeCallbacks(runnable)
                isHandlerRunning = false
            }
        }

        override fun onPause(owner: LifecycleOwner) {
            super.onPause(owner)
            this@SeekBarHandler.removeCallbacks(runnable)
            isHandlerRunning = false
        }
    }

    inner class Run:Runnable{
        override fun run() {
            isHandlerRunning = true
            val time = musicService.getCurrentPosition()
            seekBar.progress = time
            timerView.text = TimeConverter.toMinutes(time.toLong())
            post(this)
        }
    }

}
