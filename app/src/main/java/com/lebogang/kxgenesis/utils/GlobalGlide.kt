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

package com.lebogang.kxgenesis.utils

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lebogang.kxgenesis.R

object GlobalGlide {

    fun loadAudioCover(root: View, imageView: ImageView, uri:Uri?){
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
            Glide.with(root)
                    .asBitmap()
                    .load(uri)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_custom_song)
                    .into(imageView)
                    .clearOnDetach()
        }else{
            Glide.with(root)
                    .asBitmap()
                    .load(uri)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_custom_song)
                    .into(imageView)
                    .clearOnDetach()
        }
    }

    fun loadAudioCover(root: AppCompatActivity, imageView: ImageView, uri:Uri?){
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
            Glide.with(root)
                    .asBitmap()
                    .load(uri)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_custom_song)
                    .into(imageView)
                    .clearOnDetach()
        }else{
            Glide.with(root)
                    .asBitmap()
                    .load(uri)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_custom_song)
                    .into(imageView)
                    .clearOnDetach()
        }
    }

    fun loadAudioCover(root: Fragment, imageView: ImageView, uri:Uri?){
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
            Glide.with(root)
                    .asBitmap()
                    .load(uri)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_custom_song)
                    .into(imageView)
                    .clearOnDetach()
        }else{
            Glide.with(root)
                    .asBitmap()
                    .load(uri)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_custom_song)
                    .into(imageView)
                    .clearOnDetach()
        }
    }

    fun loadAlbumCoverForRecyclerView(root: View, imageView: ImageView, uri:Uri?){
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
            Glide.with(root)
                    .asBitmap()
                    .load(uri)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_custom_album)
                    .into(imageView)
                    .clearOnDetach()
        }else{
            Glide.with(root)
                    .asBitmap()
                    .load(uri)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_custom_album)
                    .into(imageView)
                    .clearOnDetach()
        }
    }

    fun loadAlbumCover(root: AppCompatActivity, imageView: ImageView, uri:Uri?){
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
            Glide.with(root)
                    .asBitmap()
                    .load(uri)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_music_record_light)
                    .into(imageView)
                    .clearOnDetach()
        }else{
            Glide.with(root)
                    .asBitmap()
                    .load(uri)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_music_record_light)
                    .into(imageView)
                    .clearOnDetach()
        }
    }

    fun loadArtistCoverForRecyclerView(root: View, imageView: ImageView, uri:Uri?){
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
            Glide.with(root)
                    .asBitmap()
                    .load(uri)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_custom_artist)
                    .into(imageView)
                    .clearOnDetach()
        }else{
            Glide.with(root)
                    .asBitmap()
                    .load(uri)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_custom_artist)
                    .into(imageView)
                    .clearOnDetach()
        }
    }

    fun loadArtistCover(root: AppCompatActivity, imageView: ImageView, uri:Uri?){
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
            Glide.with(root)
                    .asBitmap()
                    .load(uri)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_artist_light)
                    .into(imageView)
                    .clearOnDetach()
        }else{
            Glide.with(root)
                    .asBitmap()
                    .load(uri)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_artist_light)
                    .into(imageView)
                    .clearOnDetach()
        }
    }

    
}
