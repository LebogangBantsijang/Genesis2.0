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

package com.lebogang.vibe.online.stream.models

import android.net.Uri
import android.os.Parcelable
import com.lebogang.vibe.database.local.models.AbstractMusic
import kotlinx.parcelize.Parcelize

@Parcelize
data class Music(
    val id:Long,
    val title:String,
    val duration:Long,
    val explicit:Boolean,
    val releaseDate:Long,
    val streams:Int,
    val link:String,
    val artist:String,
    val artistIds:List<Long>,
    val genres:List<String>,
    val album: Album): AbstractMusic(),Parcelable {

    override fun getItemId(): Long = id

    override fun getItemTitle(): String = title

    override fun getItemArtist(): String = artist

    override fun getItemAlbum(): String = album.title

    override fun getItemImagePath(): String = album.coverMedium

    override fun getItemArtUri(): Uri = Uri.parse(album.coverMedium)

    override fun getItemContentPath(): String = link

    override fun getItemContentUri(): Uri = Uri.parse(link)

    override fun getItemDuration(): Long = duration

    override fun isItemFavourite(): Boolean = false

}
