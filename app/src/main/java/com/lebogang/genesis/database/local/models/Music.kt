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

package com.lebogang.genesis.database.local.models

import android.net.Uri
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Music")
data class Music (
    @PrimaryKey
    val id:Long,
    val albumId:Long,
    val artistId:Long,
    var title:String,
    var artist:String,
    var album:String,
    val duration:Long,
    val time:String,
    val size:String,
    val trackNumber:Int,
    val date:Long,
    var isFavourite:Boolean,
    val contentUri:String,
    var artUri:String): AbstractMusic() {

    @Ignore
    override fun getItemId(): Long = id

    @Ignore
    override fun getItemTitle(): String = title

    @Ignore
    override fun getItemArtist(): String = artist

    @Ignore
    override fun getItemAlbum(): String = album

    @Ignore
    override fun isItemFavourite(): Boolean = isFavourite

    @Ignore
    override fun getItemImagePath(): String = artUri

    @Ignore
    override fun getItemArtUri(): Uri = Uri.parse(artUri)

    @Ignore
    override fun getItemDuration(): Long = duration

    @Ignore
    override fun getItemContentPath(): String = contentUri

    @Ignore
    override fun getItemContentUri(): Uri = Uri.parse(contentUri)

}