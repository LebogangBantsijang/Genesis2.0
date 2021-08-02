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

package com.lebogang.vibe.ui.local.viewmodel

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lebogang.vibe.GApplication
import com.lebogang.vibe.database.local.MusicRepository
import com.lebogang.vibe.database.local.models.Music
import com.lebogang.vibe.database.local.scan.LocalContent
import com.lebogang.vibe.database.local.scan.MediaStoreObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicViewModel(private val musicRepository: MusicRepository): ViewModel() {
    lateinit var localContent: LocalContent

    private val mediaStoreObserver = object : MediaStoreObserver(){
        override fun onContentChanged() {
            localContent.initDatabase()
        }
    }

    fun getFavourites() = musicRepository.getFavouriteMusic().asLiveData()

    fun getMusic() = musicRepository.getMusic().asLiveData()

    fun getAlbumMusic(albumId:Long) = musicRepository.getAlbumMusic(albumId).asLiveData()

    fun getArtistMusic(artistId:Long) = musicRepository.getArtistMusic(artistId).asLiveData()

    fun getMusic(idArray: Array<Long>) = musicRepository.getMusic(idArray).asLiveData()

    fun addMusic(music: Music) = viewModelScope.launch(Dispatchers.IO) {
        musicRepository.addMusic(music)
    }

    @SuppressLint("InlinedApi")
    fun update(context:Context, music:Music) = viewModelScope.launch(Dispatchers.IO) {
        val uri = Uri.parse(music.contentUri)
        val values = ContentValues().apply {
            put(MediaStore.Audio.Media.TITLE,music.title)
            put(MediaStore.Audio.Media.ALBUM,music.album)
            put(MediaStore.Audio.Media.ARTIST,music.artist)
        }
        try {
            val results = context.contentResolver.update(uri,values, (MediaStore.Audio.Media._ID + " =?"),
                arrayOf(music.id.toString()))
            if (results>0)
                musicRepository.addMusic(music)
        }catch (e:NullPointerException){

        }
    }

    fun registerObserver(application: GApplication){
        localContent = application.localContent
        application.contentResolver.registerContentObserver(LocalContent.ResolverConstants.MUSIC_URI
            ,true,mediaStoreObserver)
    }

    fun unregisterObserver(application: GApplication){
        application.contentResolver.unregisterContentObserver(mediaStoreObserver)
    }

}
