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

package com.lebogang.genesis.interfaces

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.lebogang.genesis.data.models.Audio

interface MusicInterface {

    fun prepare(audio: Audio)

    fun play(audio: Audio)

    fun playOnline(url:String, state:OnStateChangedListener)

    fun stopOnline();

    fun getOnlineDuration():Int

    fun play()

    fun pause()

    fun stop()

    fun seekTo(position:Int)

    fun getCurrentPosition():Int

    fun skipToNext()

    fun skipToPrevious()

    fun changeRepeatMode()

    fun getRepeatMode(): RepeatSate

    fun getPlaybackState(): PlaybackState

    fun addStateChangedListener(className:String,stateChangedListener: OnStateChangedListener)

    @RequiresApi(Build.VERSION_CODES.P)
    fun enableAudioFx()

    @RequiresApi(Build.VERSION_CODES.P)
    fun disableAudioFx()

    @RequiresApi(Build.VERSION_CODES.P)
    fun isAudioFxEnabled():Boolean

    @RequiresApi(Build.VERSION_CODES.P)
    fun getEffectLevel(type:AudioFxType):Int

    @RequiresApi(Build.VERSION_CODES.P)
    fun setEffectLevel(level: Float,type:AudioFxType)

}