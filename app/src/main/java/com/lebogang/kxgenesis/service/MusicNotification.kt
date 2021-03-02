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
import android.content.Context
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.*
import com.lebogang.kxgenesis.R
import com.lebogang.kxgenesis.data.models.Audio

class MusicNotification(private val context:Context) {
    private val channelId = "113"
    private val channelName = "GenesisMusic"
    private val channelDescription = "Enjoy your music"
    @SuppressLint("InlinedApi")
    private val channelImportance = NotificationManager.IMPORTANCE_DEFAULT
    @RequiresApi(Build.VERSION_CODES.O)
    private val channel = NotificationChannel(channelId, channelName, channelImportance).apply {
        description = channelDescription
    }
    private var isChannelCreated = false
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager
    //private val pauseIntent = Intent(this)

    private fun setChannel(){
        if (!isChannelCreated)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(channel)
                isChannelCreated = true
            }
    }

    fun createNotification(audio: Audio, token: MediaSessionCompat.Token):Notification{
        val subtitle = audio.artist + "-" + audio.album
        return NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_music_note_24dp)
                .setCategory(Notification.CATEGORY_TRANSPORT)
                .setContentTitle(audio.title)
                .setContentText(subtitle)
                .setLargeIcon(null)
                .setShowWhen(false)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(channelImportance)
                .setStyle(MediaStyle().also {
                    it.setShowCancelButton(false)
                    it.setMediaSession(token)
                    it.setShowActionsInCompactView(3)
                })
                .build()
    }

}
