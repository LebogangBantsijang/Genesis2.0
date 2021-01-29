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

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lebogang.kxgenesis.data.models.Artist
import com.lebogang.kxgenesis.data.models.Audio
import com.lebogang.kxgenesis.data.repositories.ArtistRepo
import com.lebogang.kxgenesis.data.repositories.AudioRepo
import com.lebogang.kxgenesis.viewmodels.utils.OnContentChanged
import kotlinx.coroutines.launch

class ArtistViewModel(private val artistRepo: ArtistRepo,private val audioRepo: AudioRepo)
    :ViewModel(), OnContentChanged {
    val artistLiveData : MutableLiveData<LinkedHashMap<String, Artist>> = MutableLiveData()
    val audioLiveData:MutableLiveData<LinkedHashMap<Long, Audio>> = MutableLiveData()
    private var name:String? = null

    fun registerContentObserver(){
        audioRepo.registerObserver(this)
    }

    fun unregisterContentContentObserver(){
        audioRepo.unregisterObserver()
    }

    fun getArtists() = viewModelScope.launch {
        artistLiveData.postValue(artistRepo.getArtists())
    }

    fun getArtists(artistName:String) = viewModelScope.launch {
        artistLiveData.postValue(artistRepo.getArtists(artistName))
    }

    fun getArtistAudio(artistName: String) = viewModelScope.launch {
        audioLiveData.postValue(audioRepo.getArtistAudio(artistName))
        name = artistName
    }

    override fun onChanged() {
        if (name != null)
            getArtistAudio(name!!)
    }

    class Factory(private val artistRepo: ArtistRepo,private val audioRepo: AudioRepo):ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArtistViewModel::class.java))
                return ArtistViewModel(artistRepo, audioRepo) as T
            throw IllegalArgumentException()
        }

    }
}
