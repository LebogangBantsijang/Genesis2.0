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

package com.lebogang.genesis.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lebogang.genesis.data.models.Artist
import com.lebogang.genesis.data.repositories.ArtistRepo
import com.lebogang.genesis.viewmodels.utils.OnContentChanged
import kotlinx.coroutines.launch

class ArtistViewModel(private val artistRepo: ArtistRepo)
    : ViewModel(), OnContentChanged {
    val liveData : MutableLiveData<MutableList<Artist>> = MutableLiveData()

    fun registerContentObserver(){
        artistRepo.registerObserver(this)
    }

    fun unregisterContentContentObserver(){
        artistRepo.unregisterObserver()
    }

    fun getArtists() = viewModelScope.launch {
        liveData.postValue(artistRepo.getArtists())
    }

    fun getArtists(artistName:String) = artistRepo.getArtists(artistName)


    override fun onMediaChanged() {
        getArtists()
    }

    class Factory(private val artistRepo: ArtistRepo)
        :ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArtistViewModel::class.java))
                return ArtistViewModel(artistRepo) as T
            throw IllegalArgumentException()
        }

    }
}
