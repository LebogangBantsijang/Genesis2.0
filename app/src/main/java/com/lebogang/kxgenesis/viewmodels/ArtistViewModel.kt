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
import com.lebogang.kxgenesis.network.dao.DeezerService
import com.lebogang.kxgenesis.viewmodels.utils.OnContentChanged
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArtistViewModel(private val artistRepo: ArtistRepo, private val deezerService: DeezerService)
    : ViewModel(), OnContentChanged {
    val liveData : MutableLiveData<Artist> = MutableLiveData()

    fun registerContentObserver(){
        artistRepo.registerObserver(this)
    }

    fun unregisterContentContentObserver(){
        artistRepo.unregisterObserver()
    }

    fun getArtists() = viewModelScope.launch {
        val map = artistRepo.getArtists()
        map.asIterable().forEach {
            deezerService.getArtistImage(it.key).enqueue(getCallbacks(it.value))
        }
    }

    fun getArtists(artistName:String) = viewModelScope.launch {
        val map = artistRepo.getArtists(artistName)
    }


    override fun onMediaChanged() {
        getArtists()
    }

    class Factory(private val artistRepo: ArtistRepo,private val deezerService: DeezerService)
        :ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArtistViewModel::class.java))
                return ArtistViewModel(artistRepo,deezerService) as T
            throw IllegalArgumentException()
        }

    }

    private fun getCallbacks(artist: Artist):Callback<Artist>{
        return object :Callback<Artist>{
            override fun onResponse(call: Call<Artist>, response: Response<Artist>) {
                if (response.isSuccessful){
                    liveData.postValue(response.body())
                }else
                    liveData.postValue(artist)
            }
            override fun onFailure(call: Call<Artist>, t: Throwable) {
                liveData.postValue(artist)
            }
        }
    }
}
