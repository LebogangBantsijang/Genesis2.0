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

package com.lebogang.vibe.database.local.scan

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.text.format.Formatter
import androidx.annotation.WorkerThread
import com.lebogang.vibe.database.local.AlbumRepository
import com.lebogang.vibe.database.local.ArtistRepository
import com.lebogang.vibe.database.local.GenreRepository
import com.lebogang.vibe.database.local.MusicRepository
import com.lebogang.vibe.database.local.models.Album
import com.lebogang.vibe.database.local.models.Artist
import com.lebogang.vibe.database.local.models.Genre
import com.lebogang.vibe.database.local.models.Music
import com.lebogang.vibe.utils.TimeConverter
import kotlinx.coroutines.*

class LocalContent(private val context: Context,
                   private val musicRepository: MusicRepository,
                   private val albumRepository: AlbumRepository,
                   private val artistRepository: ArtistRepository,
                   private val genreRepository: GenreRepository) {
    private val contentResolver = context.contentResolver
    private val coroutine = CoroutineScope(Job() + Dispatchers.IO)

    fun initDatabase() = coroutine.launch{
        if (musicRepository.getMusicCount() != getMusicCount()){
            musicRepository.reset()
            albumRepository.reset()
            artistRepository.reset()
            genreRepository.reset()
            loadMusic()
            loadAlbums()
            loadArtists()
            loadGenres()
        }
    }

    fun reset() = coroutine.launch {
        musicRepository.reset()
        albumRepository.reset()
        artistRepository.reset()
        genreRepository.reset()
        loadMusic()
        loadAlbums()
        loadArtists()
        loadGenres()
    }

    @SuppressLint("InlinedApi")
    @WorkerThread
    suspend fun getMusicCount():Int{
        val cursor = contentResolver.query(ResolverConstants.MUSIC_URI
            , arrayOf(MediaStore.Audio.Media._ID),ResolverConstants.IS_MUSIC
            ,null,null)
        val count = cursor?.count?:0
        cursor?.close()
        return count
    }

    /**
     * Get music from the local database and save it in to our database
     * */
    @SuppressLint("InlinedApi")
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun loadMusic(){
        val musicList = mutableListOf<Music>()
        val cursor = contentResolver.query(ResolverConstants.MUSIC_URI,
            ResolverConstants.MUSIC_PROJECTION,ResolverConstants.IS_MUSIC,
            null, null)
        cursor?.let{
            //ensure that the cursor is pointing to something
            if (it.moveToFirst())
                do { //loop through the results and add to the list
                    val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                    val albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                    val artistId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID))
                    val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                    val trackNumber = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.TRACK))
                    val duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val time = TimeConverter.toMinutes(duration)
                    val size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE))
                    val date = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED))
                    val fileSize = getFileSize(size)
                    val artUri = getArtUri(albumId)
                    val contentUri = ContentUris.withAppendedId(ResolverConstants.MUSIC_URI, id).toString()
                    val music = Music(id,albumId,artistId,title,artist,album,duration,time,fileSize,trackNumber
                        ,date ,false,contentUri, artUri)
                    musicList.add(music)
                }while (it.moveToNext())
        }
        cursor?.close()
        musicRepository.addMusic(musicList)
    }

    /**
     * Get albums from the local database and save it in to our database
     * */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun loadAlbums(){
        val albumsMap = mutableListOf<Album>()
        val cursor = contentResolver.query(ResolverConstants.ALBUM_URI,ResolverConstants.ALBUM_PROJECTION
            , null, null, null)
        cursor?.let {
            //ensure that the cursor is pointing to something
            if (it.moveToFirst())
                do {//loop through the results and add to the list
                    val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID))
                    val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM))
                    val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST))
                    val date = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums.LAST_YEAR))
                    val artUri = getArtUri(id)
                    val contentUri = ContentUris.withAppendedId(ResolverConstants.ALBUM_URI, id).toString()
                    val details = getDetails(title, true)
                    val duration = details[0]
                    val size = details[1]
                    val count = details[2].toInt()
                    val fileSize = getFileSize(size)
                    val time = TimeConverter.toGeneralTime(duration)
                    val album = Album(id,title,artist,count,time,fileSize,date,false, contentUri
                        , artUri)
                    albumsMap.add(album)
                }while (it.moveToNext())
        }
        cursor?.close()
        albumRepository.addAlbum(albumsMap)
    }

    /**
     * Get artists from the local database and save it in to our database
     * */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun loadArtists(){
        val artistMap = mutableListOf<Artist>()
        val cursor = contentResolver.query(ResolverConstants.ARTIST_URI,ResolverConstants.ARTIST_PROJECTION
            , null, null, null)
        cursor?.let {
            //ensure that the cursor is pointing to something
            if (it.moveToFirst())
                do {//loop through the results and add to the list
                    val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Artists._ID))
                    val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST))
                    val contentUri = ContentUris.withAppendedId(ResolverConstants.ARTIST_URI, id).toString()
                    val details = getDetails(title, false)
                    val duration = details[0]
                    val size = details[1]
                    val count = details[2].toInt()
                    val artUri = getArtUri(details[3])
                    val fileSize = getFileSize(size)
                    val time = TimeConverter.toGeneralTime(duration)
                    val artist = Artist(id,title,count,time,fileSize,false, contentUri, artUri)
                    artistMap.add(artist)
                }while (it.moveToNext())
        }
        cursor?.close()
        artistRepository.addArtist(artistMap)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun loadGenres(){
        val genreList = mutableListOf<Genre>()
        val genreMusicList = mutableListOf<Genre.Members>()
        val cursor = contentResolver.query(ResolverConstants.GENRE_URI,ResolverConstants.GENRE_PROJECTION
            , null, null, null)
        cursor?.let {
            if (it.moveToFirst())
                do {
                    val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Genres._ID))
                    val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres.NAME))
                    val genre = Genre(id, title)
                    genreList.add(genre)
                    val memberList = loadGenreMembers(id)
                    genreMusicList.addAll(memberList)
                }while (it.moveToNext())
        }
        cursor?.close()
        genreRepository.addGenre(genreList)
        genreRepository.addGenreMusic(genreMusicList)
    }

    private fun loadGenreMembers(genreId: Long):List<Genre.Members>{
        val list = mutableListOf<Genre.Members>()
        val cursor = contentResolver.query(ResolverConstants.getGenreMemberUri(genreId)
            , ResolverConstants.GENRE_MEMBERS_PROJECTION, null, null, null)
        cursor?.let {
            if (it.moveToFirst())
                do {
                    val musicId = cursor.getLong(
                        cursor.getColumnIndex(MediaStore.Audio.Genres.Members.AUDIO_ID))
                    val genreMembers = Genre.Members(0,genreId, musicId)
                    list.add(genreMembers)
                }while (cursor.moveToNext())
        }
        cursor?.close()
        return list
    }

    /**
     * Get additional info on albums and artists that is not available in their tables
     * We are going to loop through the Music table and acquire the required info
     *
     * - Duration: Total duration of all the songs in that specific album
     * - Size: Total size of all the songs in that specific album
     * - Count: Total songs
     *
     * @return: An array of the details
     * */
    @SuppressLint("InlinedApi")
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    private suspend fun getDetails(itemTitle:String, isAlbum:Boolean):Array<Long>{
        var duration = 0L
        var size = 0L
        var albumId = 0L
        val selection = if (isAlbum) ResolverConstants.ALBUM_TITLE else ResolverConstants.ARTIST_TITLE
        val cursor = contentResolver.query(ResolverConstants.MUSIC_URI,ResolverConstants.SUB_MUSIC_PROJECTION
            ,"$selection =? AND ${ResolverConstants.IS_MUSIC}", arrayOf(itemTitle)
            , null)
        cursor?.let {
            if (it.moveToFirst())
                do {
                    size += cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE))
                    duration += cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                }while (it.moveToNext())
        }
        val count:Long = cursor?.count?.toLong() ?: 0L
        cursor?.close()
        return arrayOf(duration,size,count,albumId)
    }

    private fun getArtUri(id:Long):String{
        return "content://media/external/audio/albumart/$id"
    }

    private fun getFileSize(size:Long):String{
        return Formatter.formatShortFileSize(context,size)
    }

    object ResolverConstants{
        @SuppressLint("InlinedApi")
        val MUSIC_PROJECTION = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATE_MODIFIED,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.TRACK)
        val MUSIC_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI!!
        const val IS_MUSIC = MediaStore.Audio.Media.IS_MUSIC
        @SuppressLint("InlinedApi")
        const val ALBUM_TITLE = MediaStore.Audio.Media.ALBUM
        @SuppressLint("InlinedApi")
        const val ARTIST_TITLE = MediaStore.Audio.Media.ARTIST
        @SuppressLint("InlinedApi")
        val SUB_MUSIC_PROJECTION = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.ALBUM_ID)

        val ALBUM_PROJECTION = arrayOf(
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.LAST_YEAR,
            MediaStore.Audio.Albums.ARTIST)
        val ALBUM_URI = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI!!

        val ARTIST_PROJECTION = arrayOf(
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS)
        val ARTIST_URI = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI!!

        val GENRE_PROJECTION = arrayOf(
            MediaStore.Audio.Genres._ID,
            MediaStore.Audio.Genres.NAME)
        val GENRE_URI = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI!!

        val GENRE_MEMBERS_PROJECTION = arrayOf(
            MediaStore.Audio.Genres.Members.AUDIO_ID)
        @SuppressLint("InlinedApi")
        fun getGenreMemberUri(genreId:Long): Uri = MediaStore.Audio.Genres.Members
            .getContentUri(MediaStore.VOLUME_EXTERNAL, genreId)
    }

    object SORT{
        const val TITLE_ASC:String = "ORDER BY title DESC"
        var TITLE_DESC:String = ""
    }

}
