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

package com.lebogang.kxgenesis.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import androidx.lifecycle.ViewModelProvider

import com.lebogang.kxgenesis.data.models.Audio
import com.lebogang.kxgenesis.data.models.Playlist
import com.lebogang.kxgenesis.data.repositories.AudioRepo
import com.lebogang.kxgenesis.data.repositories.PlaylistRepo
import com.lebogang.kxgenesis.viewmodels.utils.OnContentChanged
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistRepo: PlaylistRepo, private val audioRepo: AudioRepo):
    ViewModel(), OnContentChanged {

    val audioLiveData: MutableLiveData<LinkedHashMap<Long, Audio>> = MutableLiveData()

    fun getPlaylists(): LiveData<List<Playlist>> = playlistRepo.getPlaylists().asLiveData()

    fun getPlaylists(id:Long):LiveData<Playlist> = playlistRepo.getPlaylist(id).asLiveData()

    fun registerContentObserver(){
        audioRepo.registerObserver(this)
    }

    fun unregisterContentContentObserver(){
        audioRepo.unregisterObserver()
    }

    fun getPlaylistAudio(playlistId: Long) = viewModelScope.launch {
        val idList:List<Long>? = playlistRepo.getPlaylistAudioIds(playlistId).asLiveData().value
        audioLiveData.postValue(audioRepo.getAudio(idList))
    }

    fun insertPlaylist(playlist: Playlist) = viewModelScope.launch {
        playlistRepo.insertPlaylist(playlist)
    }

    fun deletePlaylist(playlist: Playlist) = viewModelScope.launch {
        playlistRepo.deletePlaylist(playlist)
    }

    fun clearData() = viewModelScope.launch {
        playlistRepo.clearData()
    }

    fun deletePlaylistAudio(playlistId:Long, audioId:Long) = viewModelScope.launch {
        playlistRepo.deletePlaylistAudio(playlistId,audioId)
    }

    override fun onChanged() {
        //getPlaylistAudio()
    }

    class Factory(private val playlistRepo: PlaylistRepo, private val audioRepo: AudioRepo):ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PlaylistViewModel::class.java))
                return PlaylistViewModel(playlistRepo, audioRepo) as T
            throw IllegalArgumentException()
        }

    }

}
