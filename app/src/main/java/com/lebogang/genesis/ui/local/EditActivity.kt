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

package com.lebogang.genesis.ui.local

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.lebogang.genesis.GenesisApplication
import com.lebogang.genesis.R
import com.lebogang.genesis.database.local.models.Music
import com.lebogang.genesis.databinding.ActivityEditBinding
import com.lebogang.genesis.ui.ImageLoader
import com.lebogang.genesis.ui.ModelFactory
import com.lebogang.genesis.ui.Type
import com.lebogang.genesis.ui.local.viewmodel.MusicViewModel
import com.lebogang.genesis.utils.Keys

class EditActivity : AppCompatActivity() {
    private val bind :ActivityEditBinding by lazy{ActivityEditBinding.inflate(layoutInflater)}
    private lateinit var music: Music
    private val imageLoader:ImageLoader by lazy{ImageLoader(this)}
    private val musicViewModel: MusicViewModel by lazy{ ModelFactory(application as GenesisApplication)
        .getMusicViewModel()}
    private lateinit var permissionContract: ActivityResultLauncher<IntentSenderRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.edit)
        setContentView(bind.root)
        music = intent.extras?.getParcelable(Keys.MUSIC_KEY)!!
        initToolbar()
        permissionContract = registerForActivityResult(StartIntentSenderForResult()) {
            if (it.resultCode == Activity.RESULT_OK){
                musicViewModel.update(this,music)
                Toast.makeText(this,getString(R.string.item_update),Toast.LENGTH_SHORT).show()
            }
        }
        initViews()
    }

    private fun initToolbar(){
        setSupportActionBar(bind.toolbar)
        bind.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initViews(){
        imageLoader.loadImage(music.artUri,Type.MUSIC,bind.imageView)
        bind.titleEditText.setText(music.title)
        bind.albumEditText.setText(music.album)
        bind.artistEditText.setText(music.artist)
        bind.updateButton.setOnClickListener {
            val title = bind.titleEditText.text?.toString()
            val album = bind.albumEditText.text?.toString()
            val artist = bind.artistEditText.text?.toString()
            if (!title.isNullOrBlank() or !album.isNullOrBlank() or !artist.isNullOrBlank()){
                music.title = title!!
                music.album = album!!
                music.artist = artist!!
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                    update()
                else{
                    musicViewModel.update(this,music)
                    Toast.makeText(this,getString(R.string.item_update),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun update(){
        val intent = MediaStore.createWriteRequest(contentResolver, listOf(music.getItemContentUri()))
        val request = IntentSenderRequest.Builder(intent).build()
        permissionContract.launch(request)
    }

}
