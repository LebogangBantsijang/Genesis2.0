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


import androidx.lifecycle.*
import com.lebogang.kxgenesis.room.PlaylistRepo
import com.lebogang.kxgenesis.room.models.Playlist
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistRepo: PlaylistRepo): ViewModel() {
    val liveData: LiveData<List<Playlist>> = playlistRepo.getPlaylists().asLiveData()
    val audioLiveData :MutableLiveData<List<Long>> = MutableLiveData()
    val livePlaylist :MutableLiveData<Playlist> = MutableLiveData()

    fun getPlaylists(id:Long) = viewModelScope.launch{
        livePlaylist.value = playlistRepo.getPlaylist(id)
    }

    fun getPlaylistAudio(playlistId: Long) = viewModelScope.launch {
        audioLiveData.postValue(playlistRepo.getPlaylistAudioIds(playlistId))
    }

    fun insertPlaylist(playlist: Playlist) = viewModelScope.launch {
        playlistRepo.insertPlaylist(playlist)
    }

    fun insertPlaylistAudio(playlistId: Long, audioId:Long) = viewModelScope.launch{
        playlistRepo.insertAudioPlaylist(playlistId, audioId)
    }

    fun deletePlaylist(playlist: Playlist) = viewModelScope.launch {
        playlistRepo.deletePlaylist(playlist)
    }

    fun clearData() = viewModelScope.launch {
        playlistRepo.clearData()
    }

    fun clearAudioData(playlistId: Long) = viewModelScope.launch {
        playlistRepo.clearAudioData(playlistId)
    }

    fun deletePlaylistAudio(playlistId:Long, audioId:Long) = viewModelScope.launch {
        playlistRepo.deletePlaylistAudio(playlistId,audioId)
    }

    class Factory(private val playlistRepo: PlaylistRepo):ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PlaylistViewModel::class.java))
                return PlaylistViewModel(playlistRepo) as T
            throw IllegalArgumentException()
        }

    }

}
