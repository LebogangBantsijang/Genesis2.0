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

package com.lebogang.vibe.ui.local

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lebogang.vibe.VibeApplication
import com.lebogang.vibe.R
import com.lebogang.vibe.database.local.models.Music
import com.lebogang.vibe.databinding.ActivityArtistDetailsBinding
import com.lebogang.vibe.ui.local.adapters.MusicAdapter
import com.lebogang.vibe.ui.local.dialogs.MusicOptionsDialog
import com.lebogang.vibe.ui.local.viewmodel.ArtistViewModel
import com.lebogang.vibe.ui.local.viewmodel.MusicViewModel
import com.lebogang.vibe.ui.utils.*
import com.lebogang.vibe.utils.Keys

class ArtistDetailsActivity : AppCompatActivity() {
    private val bind: ActivityArtistDetailsBinding by lazy{ActivityArtistDetailsBinding.inflate(layoutInflater)}
    private val app: VibeApplication by lazy { application as VibeApplication }
    private val musicViewModel:MusicViewModel by lazy { ModelFactory(app).getMusicViewModel()}
    private val artistViewModel: ArtistViewModel by lazy{ ModelFactory(app).getArtistViewModel()}
    private val imageLoader: ImageLoader by lazy { ImageLoader(this) }
    private val musicAdapter = MusicAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = ""
        setContentView(bind.root)
        val artistId = intent.extras?.getLong(Keys.ARTIST_KEY)!!
        initToolbar()
        initData(artistId)
        initMusic(artistId)
    }

    private fun initToolbar(){
        setSupportActionBar(bind.toolbar)
        bind.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initData(artistId:Long){
        artistViewModel.getArtist(artistId).observe(this,{
            try{
                bind.artistTitleTextView.text = it.getItemTitle()
            }catch (e:NullPointerException){
                onBackPressed()
            }
            val subTitle = it.getAudioCount() + " - "+ it.getItemDuration() + " - " + it.getItemSize()
            bind.subtitleTextView.text = subTitle
            if (it.getIsItemFavourite())
                bind.imageButton.setImageResource(R.drawable.ic_melting_heart_filled_ios)
            else
                bind.imageButton.setImageResource(R.drawable.ic_melting_heart_ios)
            imageLoader.loadImage(it.artUri, Type.ARTIST,bind.imageView)
        })
    }

    private fun initMusic(artistId:Long){
        musicAdapter.selectableBackground = Colors.getSelectableBackground(theme)
        musicAdapter.imageLoader = imageLoader
        musicAdapter.hideFavouriteButton = true
        musicAdapter.optionsClickInterface = getOptionClickInterface()
        musicViewModel.getArtistMusic(artistId).observe(this,{list->
            musicAdapter.setData(list)
            bind.progressBar.visibility = View.GONE
        })
        bind.recyclerView.layoutManager = LinearLayoutManager(this)
        bind.recyclerView.adapter = musicAdapter
    }

    private fun getOptionClickInterface() = object : ItemOptionsInterface {
        override fun onOptionsClick(item: Any) {
            val musicOptionsDialog = MusicOptionsDialog()
            musicOptionsDialog.music = item as Music
            musicOptionsDialog.showExtraOption = false
            musicOptionsDialog.showNow(supportFragmentManager,null)
        }
    }

}
