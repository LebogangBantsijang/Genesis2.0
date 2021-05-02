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

package com.lebogang.genesis.service

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.lebogang.genesis.GenesisApplication
import com.lebogang.genesis.data.models.Audio
import com.lebogang.genesis.interfaces.*
import com.lebogang.genesis.room.StatisticsRepo
import com.lebogang.genesis.settings.PlayerSettings

class MusicService : Service(), MusicInterface, AudioManager.OnAudioFocusChangeListener,
        MediaPlayer.OnCompletionListener {
    private val binder = ServiceBinder()
    private val managePlayers :ManagePlayers by lazy { ManagePlayers(this@MusicService
        , this@MusicService, this@MusicService) }
    private val broadcastMusicReceiver:MusicReceiver by lazy { MusicReceiver(this) }
    private val statisticsRepo:StatisticsRepo by lazy { (application as GenesisApplication).statisticsRepo }
    private val playerSettings:PlayerSettings by lazy{ PlayerSettings(this) }
    private val hashMap = HashMap<String, OnStateChangedListener>()
    private var playbackState = PlaybackState.NONE
    private var repeatSate = RepeatSate.REPEAT_NONE
    private val foreGroundId = 546
    private val musicQueue :MusicQueue by lazy {(application as GenesisApplication).musicQueue}

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        registerReceiver(broadcastMusicReceiver, IntentFilter().apply {
            addAction(SKIP_PREV_ACTION)
            addAction(PLAY_ACTION)
            addAction(PAUSE_ACTION)
            addAction(SKIP_NEXT_ACTION)
        })
        repeatSate = playerSettings.getRepeatMode()
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
        managePlayers.releaseFx()
    }

    inner class ServiceBinder:Binder(){
        fun getService():MusicService{
            return this@MusicService
        }
    }

    override fun prepare(audio: Audio) {
        //handle focus and media player
        managePlayers.prepare(audio)
        //player callbacks
        playbackState = PlaybackState.PREPARED
        hashMap.forEach { it.value.onPlaybackChanged(playbackState) }
        startForeground(foreGroundId, managePlayers.createNotification(audio, playbackState))
    }

    override fun play(audio: Audio) {
        musicQueue.currentAudio.value = audio
        //handle focus and media player
        managePlayers.play(audio)
        //player callbacks
        playbackState = PlaybackState.PLAYING
        hashMap.forEach { it.value.onPlaybackChanged(playbackState) }
        //notification and update stats repo
        startForeground(foreGroundId, managePlayers.createNotification(audio, playbackState))
        statisticsRepo.insertStat(audio)
    }

    override fun playOnline(url:String, state:OnStateChangedListener) {
        if (playbackState == PlaybackState.PLAYING)
            pause()
        managePlayers.playOnline(url, state)
    }

    override fun getOnlineDuration(): Int {
        return managePlayers.getOnlineDuration()
    }

    override fun stopOnline() {
        managePlayers.stopOnline()
    }

    override fun pause() {
        //handle focus and media player
        managePlayers.pause()
        //player callbacks & notifications
        playbackState = PlaybackState.PAUSED
        hashMap.forEach { it.value.onPlaybackChanged(playbackState) }
        startForeground(foreGroundId, managePlayers.createNotification(musicQueue.currentAudio.value!!, playbackState))
    }

    override fun play() {
        //handle focus and media player
        managePlayers.play()
        //player callbacks & notifications
        playbackState = PlaybackState.PLAYING
        hashMap.forEach { it.value.onPlaybackChanged(playbackState) }
        startForeground(foreGroundId, managePlayers.createNotification(musicQueue.currentAudio.value!!, playbackState))
    }

    override fun stop() {
        //handle focus and media player
        managePlayers.stop()
        unregisterReceiver(broadcastMusicReceiver)
        //player callbacks & notifications
        playerSettings.setRepeatMode(repeatSate)
        hashMap.clear()
        stopForeground(true)
        stopSelf()
    }

    fun togglePlayPause() {
        if (playbackState == PlaybackState.PLAYING)
            pause()
        else
            play()
    }

    override fun skipToNext() {
        managePlayers.abandonFocus()
        playbackState = PlaybackState.SKIPPING
        hashMap.forEach { it.value.onPlaybackChanged(playbackState) }
        handleSkipping(true)
    }

    override fun skipToPrevious() {
        managePlayers.abandonFocus()
        playbackState = PlaybackState.SKIPPING
        hashMap.forEach { it.value.onPlaybackChanged(playbackState) }
        handleSkipping(false)
    }

    private fun handleSkipping(isSkippingNext:Boolean){
        if (repeatSate == RepeatSate.SHUFFLE_ALL){
            play(musicQueue.getRandomAudio())
            return
        }
        if (isSkippingNext) play(musicQueue.getNext())
        else play(musicQueue.getPrevious())
    }

    override fun seekTo(position: Int) {
        managePlayers.seekTo(position)
    }

    override fun getCurrentPosition(): Int {
        return managePlayers.getCurrentPosition()
    }

    override fun changeRepeatMode() {
        repeatSate = when(repeatSate){
            RepeatSate.REPEAT_NONE-> RepeatSate.REPEAT_ONE
            RepeatSate.REPEAT_ONE-> RepeatSate.REPEAT_ALL
            RepeatSate.REPEAT_ALL-> RepeatSate.SHUFFLE_ALL
            RepeatSate.SHUFFLE_ALL -> RepeatSate.REPEAT_NONE
        }
        hashMap.forEach { it.value.onRepeatModeChange(repeatSate) }
    }

    override fun getRepeatMode(): RepeatSate {
        return repeatSate
    }

    override fun getPlaybackState(): PlaybackState {
        return playbackState
    }

    override fun addStateChangedListener(className:String, stateChangedListener: OnStateChangedListener) {
        hashMap[className] = stateChangedListener
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun enableAudioFx() {
        managePlayers.enableAudioFx()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun disableAudioFx() {
        managePlayers.disableAudioFx()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun isAudioFxEnabled(): Boolean {
        return managePlayers.isAudioFxEnabled()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun getEffectLevel(type: AudioFxType): Int {
        return managePlayers.getEffectLevel(type)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun setEffectLevel(level: Float, type: AudioFxType) {
        managePlayers.setEffectLevel(level, type)
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when(focusChange){
            AudioManager.AUDIOFOCUS_LOSS,AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> pause()
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        playbackState = PlaybackState.COMPLETE
        hashMap.forEach { it.value.onPlaybackChanged(playbackState) }
        when(repeatSate){
            RepeatSate.REPEAT_ONE -> play(musicQueue.currentAudio.value!!)
            RepeatSate.REPEAT_ALL -> play(musicQueue.getNext())
            RepeatSate.REPEAT_NONE -> { pause() }
            RepeatSate.SHUFFLE_ALL -> play(musicQueue.getRandomAudio())
        }
    }
}
