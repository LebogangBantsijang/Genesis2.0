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

package com.lebogang.kxgenesis.data.repositories.local

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore.Audio.Media._ID
import android.provider.MediaStore.Audio.Media.TITLE
import android.provider.MediaStore.Audio.Media.ARTIST
import android.provider.MediaStore.Audio.Media.ALBUM
import android.provider.MediaStore.Audio.Media.ALBUM_ID
import android.provider.MediaStore.Audio.Media.DURATION
import android.provider.MediaStore.Audio.Media.SIZE
import android.provider.MediaStore.Audio.Media.DATE_ADDED
import android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
import android.text.format.Formatter
import com.lebogang.kxgenesis.data.models.Audio
import com.lebogang.kxgenesis.data.utils.LocalArtUri
import com.lebogang.kxgenesis.settings.AppPreferences
import com.lebogang.kxgenesis.utils.TimeConverter

const val SORT_AUDIO_BY_TITLE = "$TITLE ASC"
const val SORT_AUDIO_BY_DATE = "$DATE_ADDED DESC"

class LocalAudio(private val context:Context) {

    val contentResolver :ContentResolver = context.applicationContext.contentResolver
    @SuppressLint("InlinedApi")
    private val projection = arrayOf(_ID, TITLE, ARTIST, ALBUM, ALBUM_ID, DURATION, SIZE)
    private val appPreferences = AppPreferences(context)
    @SuppressLint("InlinedApi")
    private val selection = "$DURATION >=?"

    /**
     * Get all audio
     * */
    suspend fun getAudio():MutableList<Audio>{
        val sortOrder = appPreferences.getSortOrder()
        val durationFilter = appPreferences.getAudioDurationFilter()
        val cursor = contentResolver.query(EXTERNAL_CONTENT_URI, projection,
                selection, arrayOf(durationFilter.toString()), sortOrder)
        return loopCursor(cursor, null)
    }

    /**
     * Get audio with the specified album name
     * @param albumName
     * */
    @SuppressLint("InlinedApi")
    suspend fun getAlbumAudio(albumName:String):MutableList<Audio>{
        val sortOrder = appPreferences.getSortOrder()
        val durationFilter = appPreferences.getAudioDurationFilter()
        val selection = "$DURATION >=? AND $ALBUM =?"
        val cursor = contentResolver.query(EXTERNAL_CONTENT_URI, projection,
                selection, arrayOf(durationFilter.toString(), albumName), sortOrder)
        return loopCursor(cursor, null)
    }

    /**
     * Get audio with the specified artist name
     * @param artistName
     * */
    @SuppressLint("InlinedApi")
    suspend fun getArtistAudio(artistName:String):MutableList<Audio>{
        val sortOrder = appPreferences.getSortOrder()
        val durationFilter = appPreferences.getAudioDurationFilter()
        val selection = "$DURATION >=? AND $ARTIST =?"
        val cursor = contentResolver.query(EXTERNAL_CONTENT_URI, projection,
                selection, arrayOf(durationFilter.toString(), artistName), sortOrder)
        return loopCursor(cursor, null)
    }

    /**
     * Get audio that matches the specified Uir
     * @param uri
     * */
    suspend fun getAudio(uri: Uri):MutableList<Audio>{
        val sortOrder = appPreferences.getSortOrder()
        val cursor = contentResolver.query(uri, projection,null,
                null , sortOrder)
        return loopCursor(cursor, null)
    }

    /**
     * Get audio that matches the specified ids
     * @param audioIdList: list of audio ids
     * */
    suspend fun getAudio(audioIdList:List<Long>?):MutableList<Audio>{
        val durationFilter = appPreferences.getAudioDurationFilter()
        val cursor = contentResolver.query(EXTERNAL_CONTENT_URI, projection,
                selection, arrayOf(durationFilter.toString()), null)
        return loopCursor(cursor, audioIdList)
    }

    /**
     * delete audio
     * @param audio
     * */
    suspend fun deleteAudio(audio: Audio){
        contentResolver.delete(EXTERNAL_CONTENT_URI, "$_ID =?", arrayOf(audio.id.toString()))
    }

    /**
     * update audio
     * @param audio
     * @param contentValues: new values
     * */
    suspend fun updateAudio(audio: Audio, contentValues: ContentValues){
        contentResolver.update(audio.uri, contentValues, "$_ID =?", arrayOf(audio.id.toString()))
    }

    /**
     * Loop through the cursor and build media objects
     * @param cursor
     * @param idList: if id is not null then return items with the id in the list
     * */
    @SuppressLint("InlinedApi")
    private fun loopCursor(cursor: Cursor?,idList: List<Long>?):MutableList<Audio>{
        val linkedHashMap = LinkedHashMap<Long,Audio>()
        cursor?.let {
            if (cursor.moveToFirst()){
                do {
                    val id = cursor.getLong(cursor.getColumnIndex(_ID))
                    val title = cursor.getString(cursor.getColumnIndex(TITLE))
                    val artist = cursor.getString(cursor.getColumnIndex(ARTIST))
                    val album = cursor.getString(cursor.getColumnIndex(ALBUM))
                    val duration = cursor.getLong(cursor.getColumnIndex(DURATION))
                    val durationFormatted = TimeConverter.toMinutes(duration)
                    val size = Formatter.formatShortFileSize(context, cursor.getLong(cursor.getColumnIndex(SIZE)))
                    val albumArt = LocalArtUri.getAlbumArt(cursor.getLong(cursor.getColumnIndex(ALBUM_ID)))
                    val uri = ContentUris.withAppendedId(EXTERNAL_CONTENT_URI, id)
                    val audio = Audio(id, title, artist, album, duration, durationFormatted
                            , size, albumArt, uri)
                    if (idList != null && idList.contains(id))
                        linkedHashMap[id] = audio
                    else
                        linkedHashMap[id] = audio
                }while (cursor.moveToNext())
            }
            cursor.close()
        }
        return linkedHashMap.values.toMutableList()
    }

}
