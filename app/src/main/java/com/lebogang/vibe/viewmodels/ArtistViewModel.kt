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

package com.lebogang.vibe.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lebogang.vibe.data.models.Artist
import com.lebogang.vibe.data.repositories.ArtistRepo
import com.lebogang.vibe.viewmodels.utils.OnContentChanged
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

}
