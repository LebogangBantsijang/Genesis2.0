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

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.data.models.Audio
import com.lebogang.kxgenesis.service.utils.PlaybackState
import com.lebogang.kxgenesis.ui.PlayerActivity

class MusicNotification(private val context:Context) {
    private val channelId = "113"
    private val channelName = "GenesisMusic"
    private val channelDescription = "Enjoy your music"
    @SuppressLint("InlinedApi")
    private val channelImportance = NotificationManager.IMPORTANCE_LOW
    @RequiresApi(Build.VERSION_CODES.O)
    private val channel = NotificationChannel(channelId, channelName, channelImportance).apply {
        description = channelDescription
        enableLights(false)
        enableVibration(false)
    }
    private var isChannelCreated = false
    private val notificationManager = context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager
    init {
        setChannel()
    }

    private fun setChannel(){
        if (!isChannelCreated)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(channel)
                isChannelCreated = true
            }
    }

    fun createNotification(audio: Audio, playbackState: PlaybackState):Notification{
        val subtitle = audio.artist + "-" + audio.album
        val  builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_music_note_24dp)
                .setCategory(Notification.CATEGORY_TRANSPORT)
                .setContentTitle(audio.title)
                .setContentText(subtitle)
                .setLargeIcon(getBitmap(audio.albumArtUri))
                .setShowWhen(false)
                .setNotificationSilent()
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(channelImportance)
                .setContentIntent(PendingIntent.getActivity(context, 146,
                        Intent(context, PlayerActivity::class.java)
                        ,PendingIntent.FLAG_UPDATE_CURRENT))
                .setStyle(MediaStyle().also {
                    it.setShowCancelButton(false)
                    it.setShowActionsInCompactView(0,1,2)
                })
        builder.addAction(R.drawable.ic_round_navigate_before_24, "",
                PendingIntent.getBroadcast(context, 0, Intent(SKIP_PREV_ACTION), PendingIntent.FLAG_UPDATE_CURRENT))
        if (playbackState == PlaybackState.PLAYING)
            builder.addAction(R.drawable.ic_round_pause_24, "",
                    PendingIntent.getBroadcast(context, 0, Intent(PAUSE_ACTION), PendingIntent.FLAG_UPDATE_CURRENT))
        else
            builder.addAction(R.drawable.ic_round_play_arrow_24, "",
                    PendingIntent.getBroadcast(context, 0, Intent(PLAY_ACTION), PendingIntent.FLAG_UPDATE_CURRENT))
        builder.addAction(R.drawable.ic_round_navigate_next_24, "",
                PendingIntent.getBroadcast(context, 0, Intent(SKIP_NEXT_ACTION), PendingIntent.FLAG_UPDATE_CURRENT))
        return builder.build()
    }

    private fun getBitmap(uri:Uri): Bitmap {
        return try {
            val inputStream = context.applicationContext.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        }catch (e:Exception){
            BitmapFactory.decodeResource(context.applicationContext.resources, R.raw.default_bg)
        }
    }
}
