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

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import androidx.annotation.RequiresApi

class ManageFocus(private val context: Context, private val listener : AudioManager.OnAudioFocusChangeListener) {
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val focusAttributes = AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .setLegacyStreamType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .build()
    @RequiresApi(Build.VERSION_CODES.O)
    private val focus = createAudioFocusRequest()

    fun requestFocus(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            audioManager.requestAudioFocus(focus)
        else
            audioManager.requestAudioFocus(listener, AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN)
    }

    fun abandonFocus(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            audioManager.abandonAudioFocusRequest(focus)
        else
            audioManager.abandonAudioFocus(listener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createAudioFocusRequest():AudioFocusRequest{
        return AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setOnAudioFocusChangeListener(listener)
            .setAudioAttributes(focusAttributes)
            .build()
    }

}
