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

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.lebogang.kxgenesis.service.utils.OnSateChangedListener
import com.lebogang.kxgenesis.service.utils.PlaybackState
import com.lebogang.kxgenesis.service.utils.RepeatSate
import com.lebogang.kxgenesis.service.utils.ShuffleSate
import com.lebogang.kxgenesis.ui.helpers.PlayerHelper

class ManageServiceConnection(private val activity: AppCompatActivity): OnSateChangedListener {
    private val intent = Intent(activity,MusicService::class.java)
    lateinit var musicService: MusicService
    private val connectionCallback = getConnection()

    init {
        activity.lifecycle.addObserver(StateObserver())
    }

    private fun getConnection():ServiceConnection{
        return object :ServiceConnection{
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                musicService = (service as MusicService.ServiceBinder).getService()
                (activity as PlayerHelper).onServiceReady(musicService)
                musicService.addStateChangedListener(activity.javaClass.name
                        ,this@ManageServiceConnection)
                if (Queue.currentAudio.value != null){
                    if (musicService.getPlaybackState() == PlaybackState.NONE)
                        musicService.prepare(Queue.currentAudio.value!!)
                    else
                        onPlaybackChanged(musicService.getPlaybackState())
                }
                onRepeatModeChange(musicService.getRepeatMode())
                onShuffleModeChange(musicService.getShuffleMode())
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                //I don't know what to do here
                //If we get here everything falls apart
            }
        }
    }

    inner class StateObserver:DefaultLifecycleObserver{

        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)
            activity.bindService(intent, connectionCallback, Context.BIND_AUTO_CREATE)
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            activity.unbindService(connectionCallback)
        }

    }

    override fun onPlaybackChanged(playbackState: PlaybackState) {
        (activity as PlayerHelper).onPlaybackChanged(playbackState)
    }

    override fun onRepeatModeChange(repeatSate: RepeatSate) {
        (activity as PlayerHelper).onRepeatModeChange(repeatSate)
    }

    override fun onShuffleModeChange(shuffleSate: ShuffleSate) {
        (activity as PlayerHelper).onShuffleModeChange(shuffleSate)
    }

}
