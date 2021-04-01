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

package com.lebogang.genesis.utils

import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lebogang.genesis.R

object GlobalGlide {

    @Suppress("IMPLICIT_CAST_TO_ANY")
    fun loadAudioArt(root:Any, indicator: GlideIndicator, uri: Uri?, view:ImageView){
        when(indicator){
            GlideIndicator.LOAD_WITH_ACTIVITY ->
                loadAudioArt(root as AppCompatActivity, uri, view)
            GlideIndicator.LOAD_WITH_VIEW ->
                loadAudioArt(root as View, uri, view)
            GlideIndicator.LOAD_WITH_FRAGMENT ->
                loadAudioArt(root as Fragment, uri, view)
        }
    }

    private fun loadAudioArt(root:AppCompatActivity, uri: Uri?, view:ImageView){
        var skipCache = false
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            skipCache = true
        Glide.with(root)
                .load(uri)
                .skipMemoryCache(skipCache)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(view.width, view.height)
                .centerCrop()
                .error(R.drawable.ic_custom_song)
                .into(view)
                .waitForLayout()
                .clearOnDetach()
    }

    private fun loadAudioArt(root:Fragment, uri: Uri?, view:ImageView){
        var skipCache = false
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            skipCache = true
        Glide.with(root)
                .load(uri)
                .skipMemoryCache(skipCache)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(view.width, view.height)
                .centerCrop()
                .error(R.drawable.ic_custom_song)
                .into(view)
                .waitForLayout()
                .clearOnDetach()
    }

    private fun loadAudioArt(root:View, uri: Uri?, view:ImageView){
        var skipCache = false
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            skipCache = true
        Glide.with(root)
                .load(uri)
                .skipMemoryCache(skipCache)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(view.width, view.height)
                .centerCrop()
                .error(R.drawable.ic_custom_song)
                .into(view)
                .waitForLayout()
                .clearOnDetach()
    }



    fun loadAudioCover(root: View, imageView: ImageView, uri:Uri?){

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            Glide.with(root as View)
                    .load(uri)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_custom_song)
                    .into(imageView)
                    .waitForLayout()
                    .clearOnDetach()
        }else{
            Glide.with(root)
                    .load(uri)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_custom_song)
                    .into(imageView)
                    .waitForLayout()
                    .clearOnDetach()
        }
    }

    fun loadGif(root: AppCompatActivity, imageView: ImageView){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            Glide.with(root)
                .asGif()
                .load(R.raw.gif_2)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(imageView.width, imageView.height)
                .centerCrop()
                .into(imageView)
                .waitForLayout()
                .clearOnDetach()
        }else{
            Glide.with(root)
                .asGif()
                .load(R.raw.gif_2)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(imageView.width, imageView.height)
                .centerCrop()
                .into(imageView)
                .waitForLayout()
                .clearOnDetach()
        }
    }

    fun loadAudioCover(root: AppCompatActivity, imageView: ImageView, uri:Uri?){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            Glide.with(root)
                    .load(uri)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_custom_song)
                    .into(imageView)
                    .waitForLayout()
                    .clearOnDetach()
        }else{
            Glide.with(root)
                    .load(uri)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_custom_song)
                    .into(imageView)
                    .waitForLayout()
                    .clearOnDetach()
        }
    }

    fun loadAudioBackground(root: AppCompatActivity, imageView: ImageView, uri:Uri?){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            Glide.with(root)
                    .load(uri)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_music)
                    .into(imageView)
                    .waitForLayout()
                    .clearOnDetach()
        }else{
            Glide.with(root)
                    .load(uri)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_music)
                    .into(imageView)
                    .waitForLayout()
                    .clearOnDetach()
        }
    }

    fun loadAudioCover(root: Fragment, imageView: ImageView, uri:Uri?){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            Glide.with(root)
                    .load(uri)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_custom_song)
                    .into(imageView)
                    .waitForLayout()
                    .clearOnDetach()
        }else{
            Glide.with(root)
                    .load(uri)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_custom_song)
                    .into(imageView)
                    .waitForLayout()
                    .clearOnDetach()
        }
    }

    fun loadAlbumCoverForRecyclerView(root: View, imageView: ImageView, uri:Uri?){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            Glide.with(root)
                    .load(uri)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_custom_album)
                    .into(imageView)
                    .waitForLayout()
                    .clearOnDetach()
        }else{
            Glide.with(root)
                    .load(uri)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_custom_album)
                    .into(imageView)
                    .waitForLayout()
                    .clearOnDetach()
        }
    }

    fun loadAlbumCover(root: Fragment, imageView: ImageView, uri:Uri?){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            Glide.with(root)
                .load(uri)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(imageView.width, imageView.height)
                .centerCrop()
                .error(R.drawable.ic_music_record)
                .into(imageView)
                .waitForLayout()
                .clearOnDetach()
        }else{
            Glide.with(root)
                .load(uri)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(imageView.width, imageView.height)
                .centerCrop()
                .error(R.drawable.ic_music_record)
                .into(imageView)
                .waitForLayout()
                .clearOnDetach()
        }
    }

    fun loadArtistCoverForRecyclerView(root: View, imageView: ImageView, uri:Uri?){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            Glide.with(root)
                    .load(uri)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_custom_artist)
                    .into(imageView)
                    .waitForLayout()
                    .clearOnDetach()
        }else{
            Glide.with(root)
                    .load(uri)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(imageView.width, imageView.height)
                    .centerCrop()
                    .error(R.drawable.ic_custom_artist)
                    .into(imageView)
                    .waitForLayout()
                    .clearOnDetach()
        }
    }

    fun loadArtistCover(root: Fragment, imageView: ImageView, uri:Uri?){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            Glide.with(root)
                .load(uri)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(imageView.width, imageView.height)
                .centerCrop()
                .error(R.drawable.ic_user)
                .into(imageView)
                .waitForLayout()
                .clearOnDetach()
        }else{
            Glide.with(root)
                .load(uri)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(imageView.width, imageView.height)
                .centerCrop()
                .error(R.drawable.ic_user)
                .into(imageView)
                .waitForLayout()
                .clearOnDetach()
        }
    }

    enum class GlideIndicator{
        LOAD_WITH_VIEW, LOAD_WITH_FRAGMENT, LOAD_WITH_ACTIVITY
    }

}
