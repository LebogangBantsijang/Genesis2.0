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

package com.lebogang.kxgenesis.data.repositories

import android.content.Context
import com.lebogang.kxgenesis.data.models.Album
import com.lebogang.kxgenesis.data.repositories.local.LocalAlbum

class AlbumRepo(val context: Context) {
    private val localAlbumRepo = LocalAlbum(context)

    fun getAlbums():LinkedHashMap<String, Album>{
        return localAlbumRepo.getAlbums()
    }

    fun getAlbums(albumName:String):LinkedHashMap<String, Album>{
        return localAlbumRepo.getAlbums(albumName)
    }

}