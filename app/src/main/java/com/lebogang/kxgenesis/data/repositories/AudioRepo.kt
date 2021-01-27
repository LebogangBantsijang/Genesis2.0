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

package com.lebogang.kxgenesis.data.repositories

import android.content.ContentValues
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import com.lebogang.kxgenesis.data.models.Audio
import com.lebogang.kxgenesis.data.repositories.local.LocalAudio

class AudioRepo(val context: Context){
    private val localAudioRepo = LocalAudio(context)
    private val contentObserver = getContentObserver()

    fun getAudio():LinkedHashMap<Long, Audio>{
        return localAudioRepo.getAudio()
    }

    fun getAlbumAudio(albumName:String):LinkedHashMap<Long, Audio>{
        return localAudioRepo.getAlbumAudio(albumName)
    }

    fun getArtistAudio(artistName:String):LinkedHashMap<Long, Audio>{
        return localAudioRepo.getArtistAudio(artistName)
    }

    fun getAudio(uri: Uri):LinkedHashMap<Long, Audio>{
        return localAudioRepo.getAudio(uri)
    }

    fun getAudio(audioIdList:List<Long>):LinkedHashMap<Long, Audio>{
        return localAudioRepo.getAudio(audioIdList)
    }

    fun updateAudio(audio: Audio, contentValues: ContentValues){
        localAudioRepo.updateAudio(audio, contentValues)
    }

    fun deleteAudio(audio: Audio){
        localAudioRepo.deleteAudio(audio)
    }

    //not finished here
    fun registerObserver(){
        localAudioRepo.contentResolver.registerContentObserver(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            , true, contentObserver)
    }

    fun unregisterObserver(){
        localAudioRepo.contentResolver.unregisterContentObserver(contentObserver)
    }

    private fun getContentObserver(): ContentObserver {
        return object : ContentObserver(Handler(Looper.getMainLooper())){
            override fun deliverSelfNotifications(): Boolean {
                return super.deliverSelfNotifications()
            }

            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
            }

            override fun onChange(selfChange: Boolean, uri: Uri?) {
                super.onChange(selfChange, uri)
            }

            override fun onChange(selfChange: Boolean, uri: Uri?, flags: Int) {
                super.onChange(selfChange, uri, flags)
            }

            override fun onChange(selfChange: Boolean, uris: MutableCollection<Uri>, flags: Int) {
                super.onChange(selfChange, uris, flags)
            }
        }
    }

}
