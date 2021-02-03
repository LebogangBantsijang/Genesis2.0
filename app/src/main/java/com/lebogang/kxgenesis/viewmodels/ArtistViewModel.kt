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
import com.lebogang.kxgenesis.data.repositories.ArtistRepo
import com.lebogang.kxgenesis.data.repositories.AudioRepo
import com.lebogang.kxgenesis.network.DeezerRepo
import com.lebogang.kxgenesis.network.dao.DeezerService
import com.lebogang.kxgenesis.network.model.DeezerArtistModel
import com.lebogang.kxgenesis.viewmodels.utils.OnContentChanged
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArtistViewModel(private val artistRepo: ArtistRepo, private val deezerService: DeezerService) : ViewModel(), OnContentChanged {
    val liveData : MutableLiveData<LinkedHashMap<String, Artist>> = MutableLiveData(LinkedHashMap())
    private var size = 0
    private var currentMap = LinkedHashMap<String, Artist>()

    fun registerContentObserver(){
        artistRepo.registerObserver(this)
    }

    fun unregisterContentContentObserver(){
        artistRepo.unregisterObserver()
    }

    fun getArtists() = viewModelScope.launch {
        val map = artistRepo.getArtists()
        size = map.size
        map.asIterable().forEach {
            deezerService.getArtistImage(it.key).enqueue(getCallbacks(it.value))
        }
    }

    fun getArtists(artistName:String) = viewModelScope.launch {
        val map = artistRepo.getArtists(artistName)
        size = map.size
        map.asIterable().forEach {
            deezerService.getArtistImage(it.key).enqueue(getCallbacks(it.value))
        }
    }


    override fun onMediaChanged() {
        getArtists()
    }

    class Factory(private val artistRepo: ArtistRepo,private val deezerService: DeezerService):ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArtistViewModel::class.java))
                return ArtistViewModel(artistRepo,deezerService) as T
            throw IllegalArgumentException()
        }

    }

    private fun getCallbacks(artist: Artist):Callback<DeezerArtistModel>{
        return object :Callback<DeezerArtistModel>{
            override fun onResponse(call: Call<DeezerArtistModel>, response: Response<DeezerArtistModel>) {
                if (response.isSuccessful){
                    artist.deezerArtistModel = response.body()
                    currentMap[artist.title] = artist
                    if (size == currentMap.size)
                        liveData.postValue(currentMap)
                }
            }
            override fun onFailure(call: Call<DeezerArtistModel>, t: Throwable) {
                currentMap[artist.title] = artist
                if (size == currentMap.size)
                    liveData.postValue(currentMap)
            }
        }
    }
}
