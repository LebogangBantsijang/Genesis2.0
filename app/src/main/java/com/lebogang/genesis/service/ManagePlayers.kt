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
import android.media.AsyncPlayer
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.audiofx.DynamicsProcessing
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.lebogang.genesis.data.models.Audio
import com.lebogang.genesis.interfaces.AudioFxType

class ManagePlayers(private val context: Context, listener:AudioManager.OnAudioFocusChangeListener
                    , completion:MediaPlayer.OnCompletionListener ) :ManageNotifications(context, listener) {

    private var mediaPlayer = MediaPlayer().apply {
        setOnCompletionListener(completion)
        setAudioAttributes(focusAttributes)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private val config = DynamicsProcessing.Config.Builder(0,1,true,
            1, true, 1, true,1,
            true)
            .build()

    @RequiresApi(Build.VERSION_CODES.P)
    private var dynamicsProcessing :DynamicsProcessing? = null

    fun prepare(audio: Audio) {
        requestFocus()
        mediaPlayer.reset()
        mediaPlayer.setDataSource(context,audio.getUri())
        mediaPlayer.prepare()
    }

    fun play(audio: Audio) {
        requestFocus()
        mediaPlayer.reset()
        mediaPlayer.setDataSource(context,audio.getUri())
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    fun play(url:String){
        mediaPlayer.setDataSource(url)
    }

    fun play() {
        requestFocus()
        mediaPlayer.start()
    }

    fun pause() {
        abandonFocus()
        mediaPlayer.pause()
    }

    fun stop() {
        abandonFocus()
        mediaPlayer.stop()
        mediaPlayer.reset()
        mediaPlayer.release()
    }

    fun seekTo(position: Int) {
        mediaPlayer.seekTo(position)
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun enableAudioFx(){
        if (dynamicsProcessing == null)
            dynamicsProcessing = DynamicsProcessing(100, mediaPlayer.audioSessionId, config)
        dynamicsProcessing?.enabled = true
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun disableAudioFx(){
        dynamicsProcessing?.enabled = false
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun isAudioFxEnabled():Boolean{
        if (dynamicsProcessing == null)
            return false
        return dynamicsProcessing!!.enabled
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun setEffectLevel(level: Float,type:AudioFxType){
        when(type){
            AudioFxType.PRE_EQ -> {
                val eq = dynamicsProcessing!!.getPreEqByChannelIndex(0).apply {
                    getBand(0).gain = level
                }
                dynamicsProcessing!!.setPreEqAllChannelsTo(eq)
            }
            AudioFxType.COMPRESSOR -> {
                val compressor = dynamicsProcessing!!.getMbcByChannelIndex(0).apply {
                    getBand(0).preGain = level
                    getBand(0).postGain = level
                }
                dynamicsProcessing!!.setMbcAllChannelsTo(compressor)
            }
            AudioFxType.POST_EQ -> {
                val eq = dynamicsProcessing!!.getPreEqByChannelIndex(0).apply {
                    getBand(0).gain = level
                }
                dynamicsProcessing!!.setPostEqAllChannelsTo(eq)
            }
            AudioFxType.LIMITER -> {
                val limiter = dynamicsProcessing!!.getLimiterByChannelIndex(0).apply {
                    postGain = level
                }
                dynamicsProcessing!!.setLimiterAllChannelsTo(limiter)
            }
            AudioFxType.MASTER_GAIN->{
                dynamicsProcessing!!.setInputGainAllChannelsTo(level)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun getEffectLevel(type:AudioFxType):Int{
        return when(type){
            AudioFxType.PRE_EQ -> {
                dynamicsProcessing!!.getPreEqByChannelIndex(0).getBand(0).gain.toInt()
            }
            AudioFxType.COMPRESSOR -> {
                dynamicsProcessing!!.getMbcByChannelIndex(0).getBand(0).postGain.toInt()
            }
            AudioFxType.POST_EQ -> {
                dynamicsProcessing!!.getPostEqByChannelIndex(0).getBand(0).gain.toInt()
            }
            AudioFxType.LIMITER -> {
                dynamicsProcessing!!.getLimiterByChannelIndex(0).postGain.toInt()
            }
            AudioFxType.MASTER_GAIN->{
                dynamicsProcessing!!.getInputGainByChannelIndex(0).toInt()
            }
        }
    }

    fun releaseFx(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            dynamicsProcessing?.enabled = false
            dynamicsProcessing?.release()
        }
    }
}