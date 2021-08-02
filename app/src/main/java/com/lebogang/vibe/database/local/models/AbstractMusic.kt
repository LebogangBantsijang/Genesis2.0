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

package com.lebogang.vibe.database.local.models

import android.net.Uri
import android.os.Parcelable

abstract class AbstractMusic: Parcelable {

    abstract fun getItemId():Long

    abstract fun getItemTitle():String

    abstract fun getItemArtist():String

    abstract fun getItemAlbum():String

    abstract fun getItemImagePath():String

    abstract fun getItemArtUri(): Uri

    abstract fun getItemContentPath():String

    abstract fun getItemContentUri(): Uri

    abstract fun getItemDuration():Long

    abstract fun isItemFavourite():Boolean

    /*private fun getMediaDescription(): MediaDescription = MediaDescription.Builder()
        .setMediaId(getItemId().toString())
        .setTitle(getItemTitle())
        .setSubtitle(getItemArtist())
        .setDescription(getItemAlbum())
        .setIconUri(getItemArtUri())
        .setMediaUri(getItemContentUri())
        .build()

    fun getQueueItem(): MediaSession.QueueItem =
        MediaSession.QueueItem(getMediaDescription(), getItemId())

    fun getMetadata(): MediaMetadata = MediaMetadata.Builder()
        .putString(MediaMetadata.METADATA_KEY_TITLE, getItemTitle())
        .putString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE, getItemTitle())
        .putString(MediaMetadata.METADATA_KEY_ARTIST, getItemArtist())
        .putString(MediaMetadata.METADATA_KEY_ALBUM, getItemAlbum())
        .putLong(MediaMetadata.METADATA_KEY_DURATION, getItemDuration())
        .putString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE, getItemArtist())
        .build()*/
}