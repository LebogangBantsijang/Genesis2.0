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

package com.lebogang.vibe.online.spotify.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.lebogang.vibe.utils.models.ArtistAbstract
import kotlinx.parcelize.Parcelize

@Parcelize
class Artist(
    @Expose
    val id:String,
    @Expose
    @SerializedName(value = "name")
    val title:String):ArtistAbstract(){

    override fun getItemId(): Any = id

    override fun getItemTitle(): String = title

    override fun getAudioCount(): String = ""

    override fun getItemSize(): String = ""

    override fun getIsItemFavourite(): Boolean = false

    override fun getItemContent(): String = ""

    override fun getItemArt(): String = ""

    override fun getItemDuration(): String = ""

}
