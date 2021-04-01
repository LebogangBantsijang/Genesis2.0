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

package com.lebogang.genesis.data.utils

import android.content.ContentUris
import android.net.Uri

object LocalArtUri {
    fun getAlbumArt(albumId:Long):String{
        return "content://media/external/audio/albumart/$albumId"
        //return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId).toString()
    }
}
