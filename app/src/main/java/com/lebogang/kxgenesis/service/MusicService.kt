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

package com.lebogang.kxgenesis.service

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.lebogang.kxgenesis.data.models.Audio
import com.lebogang.kxgenesis.service.utils.*

class MusicService : Service(), MusicInterface
    , AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnCompletionListener {

    private val binder = ServiceBinder()
    private val manageFocus:ManageFocus by lazy{
        ManageFocus(this, this)
    }
    private val mediaPlayer = MediaPlayer()
    private val musicNotification:MusicNotification by lazy{
        MusicNotification(this)
    }
    private val hashmap = HashMap<String, OnSateChangedListener>()
    private var playbackState = PlaybackState.NONE
    private var shuffleSate = ShuffleSate.SHUFFLE_NONE
    private var repeatSate = RepeatSate.REPEAT_NONE
    private val foreGroundId = 546

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    inner class ServiceBinder:Binder(){
        fun getService():MusicService{
            return this@MusicService
        }
    }

    override fun play(audio: Audio) {
        manageFocus.requestFocus()
        mediaPlayer.reset()
        mediaPlayer.setDataSource(this,audio.uri)
        mediaPlayer.prepare()
        mediaPlayer.start()
        playbackState = PlaybackState.PLAYING
        hashmap.forEach {
            it.value.onPlaybackChanged(playbackState)
        }
        startForeground(foreGroundId, musicNotification.createNotification(audio))
    }

    override fun pause() {
        manageFocus.abandonFocus()
        mediaPlayer.pause()
        playbackState = PlaybackState.PAUSED
        hashmap.forEach {
            it.value.onPlaybackChanged(playbackState)
        }
        stopForeground(true)
    }

    override fun play() {
        manageFocus.requestFocus()
        mediaPlayer.start()
        playbackState = PlaybackState.PLAYING
        hashmap.forEach {
            it.value.onPlaybackChanged(playbackState)
        }
        startForeground(foreGroundId, musicNotification.createNotification(Queue.currentAudio.value!!))
    }

    override fun stop() {
        manageFocus.abandonFocus()
        mediaPlayer.stop()
        playbackState = PlaybackState.STOPPED
        hashmap.forEach {
            it.value.onPlaybackChanged(playbackState)
        }
        stopForeground(true)
    }

    override fun togglePlayPause() {
        if (playbackState == PlaybackState.PLAYING)
            pause()
        else
            play()
    }

    override fun skipToNext() {
        manageFocus.abandonFocus()
        playbackState = PlaybackState.SKIPPING
        hashmap.forEach {
            it.value.onPlaybackChanged(playbackState)
        }
        if (shuffleSate == ShuffleSate.SHUFFLE_ALL)
            play(Queue.getRandomAudio())
        else
            play(Queue.getNext())
    }

    override fun skipToPrevious() {
        manageFocus.abandonFocus()
        playbackState = PlaybackState.SKIPPING
        hashmap.forEach {
            it.value.onPlaybackChanged(playbackState)
        }
        if (shuffleSate == ShuffleSate.SHUFFLE_ALL)
            play(Queue.getRandomAudio())
        else
            play(Queue.getPrevious())
    }

    override fun changeRepeatMode() {
        repeatSate = when(repeatSate){
            RepeatSate.REPEAT_NONE-> RepeatSate.REPEAT_ONE
            RepeatSate.REPEAT_ONE-> RepeatSate.REPEAT_ALL
            RepeatSate.REPEAT_ALL-> RepeatSate.REPEAT_NONE
        }
        hashmap.forEach {
            it.value.onRepeatModeChange(repeatSate)
        }
    }

    override fun changeShuffleMode() {
        shuffleSate = when(shuffleSate){
            ShuffleSate.SHUFFLE_NONE-> ShuffleSate.SHUFFLE_ALL
            ShuffleSate.SHUFFLE_ALL-> ShuffleSate.SHUFFLE_NONE
        }
        hashmap.forEach {
            it.value.onShuffleModeChange(shuffleSate)
        }
    }

    override fun getRepeatMode(): RepeatSate {
        return repeatSate
    }

    override fun getShuffleMode(): ShuffleSate {
        return shuffleSate
    }

    override fun getPlaybackState(): PlaybackState {
        return playbackState
    }

    override fun addStateChangedListener(className:String, stateChangedListener: OnSateChangedListener) {
        hashmap.put(className, stateChangedListener)
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when(focusChange){
            AudioManager.AUDIOFOCUS_LOSS,AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> pause()
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        playbackState = PlaybackState.COMPLETE
        hashmap.forEach {
            it.value.onPlaybackChanged(playbackState)
        }
        when(repeatSate){
            RepeatSate.REPEAT_ONE -> play(Queue.currentAudio.value!!)
            RepeatSate.REPEAT_ALL -> play(Queue.getNext())
            RepeatSate.REPEAT_NONE -> {
                pause()
            }
        }
    }
}
