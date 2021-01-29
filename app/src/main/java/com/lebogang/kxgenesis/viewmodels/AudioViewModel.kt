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

import android.content.ContentValues
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import com.lebogang.kxgenesis.data.models.Audio
import com.lebogang.kxgenesis.data.repositories.AudioRepo
import com.lebogang.kxgenesis.viewmodels.utils.AudioEnum
import com.lebogang.kxgenesis.viewmodels.utils.OnContentChanged
import kotlinx.coroutines.launch

class AudioViewModel(private val audioRepo: AudioRepo): ViewModel(), OnContentChanged {
    val liveData:MutableLiveData<LinkedHashMap<Long,Audio>> = MutableLiveData()
    private lateinit var which: AudioEnum
    private var name:Uri? = null

    fun registerContentObserver(){
        audioRepo.registerObserver(this)
    }

    fun unregisterContentContentObserver(){
        audioRepo.unregisterObserver()
    }

    fun updateAudio(audio: Audio, contentValues: ContentValues) = viewModelScope.launch {
        audioRepo.updateAudio(audio,contentValues)
    }

    fun deleteAudio(audio: Audio) = viewModelScope.launch {
        audioRepo.deleteAudio(audio)
    }

    fun getAudio() = viewModelScope.launch {
        liveData.postValue(audioRepo.getAudio())
    }

    fun getAudio(uri: Uri) = viewModelScope.launch {
        liveData.postValue(audioRepo.getAudio(uri))
        which = AudioEnum.URI_AUDIO
        name = uri
    }

    override fun onChanged() {
        when(which){
            AudioEnum.URI_AUDIO -> {
                if (name!= null)
                    getAudio(name!!)
            }
            else -> getAudio()
        }
    }

    class Factory(private val audioRepo: AudioRepo):ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AudioViewModel::class.java))
                return AudioViewModel(audioRepo) as T
            throw IllegalArgumentException()
        }

    }
}
