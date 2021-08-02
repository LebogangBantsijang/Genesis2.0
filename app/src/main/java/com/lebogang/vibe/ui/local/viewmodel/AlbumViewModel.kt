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

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lebogang.vibe.GApplication
import com.lebogang.vibe.database.local.AlbumRepository
import com.lebogang.vibe.database.local.models.Album
import com.lebogang.vibe.database.local.scan.LocalContent
import com.lebogang.vibe.database.local.scan.MediaStoreObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlbumViewModel(private val albumRepository: AlbumRepository):ViewModel() {
    lateinit var localContent: LocalContent

    private val mediaStoreObserver = object : MediaStoreObserver(){
        override fun onContentChanged() {
            localContent.initDatabase()
        }
    }

    fun getAlbumsPreview() = albumRepository.getAlbumsPreview().asLiveData()

    fun getAlbums() = albumRepository.getAlbums().asLiveData()

    fun getFavouriteAlbums() = albumRepository.getFavouriteAlbums().asLiveData()

    fun getAlbums(title:String) = albumRepository.getAlbums(title).asLiveData()

    fun getAlbums(id:Long):LiveData<Album> = albumRepository.getAlbums(id).asLiveData()

    fun getAlbums(nameArray:Array<String>) = albumRepository.getAlbums(nameArray).asLiveData()

    fun addAlbum(album: Album) = viewModelScope.launch(Dispatchers.IO) {
        albumRepository.addAlbum(album)
    }

    fun registerObserver(application: GApplication){
        localContent = application.localContent
        application.contentResolver.registerContentObserver(LocalContent.ResolverConstants.ALBUM_URI
            ,true,mediaStoreObserver)
    }

    fun unregisterObserver(application: GApplication){
        application.contentResolver.unregisterContentObserver(mediaStoreObserver)
    }

}
