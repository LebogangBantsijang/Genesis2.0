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

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lebogang.vibe.VibeApplication
import com.lebogang.vibe.R
import com.lebogang.vibe.database.local.models.Album
import com.lebogang.vibe.database.local.models.Artist
import com.lebogang.vibe.database.local.models.Genre
import com.lebogang.vibe.database.local.scan.LocalContent
import com.lebogang.vibe.databinding.ActivityHomeBinding
import com.lebogang.vibe.ui.*
import com.lebogang.vibe.ui.charts.ChartsActivity
import com.lebogang.vibe.ui.history.HistoryActivity
import com.lebogang.vibe.ui.local.adapters.AlbumPreviewAdapter
import com.lebogang.vibe.ui.local.adapters.ArtistPreviewAdapter
import com.lebogang.vibe.ui.local.adapters.GenrePreviewAdapter
import com.lebogang.vibe.ui.local.playlist.PlaylistActivity
import com.lebogang.vibe.ui.local.viewmodel.AlbumViewModel
import com.lebogang.vibe.ui.local.viewmodel.ArtistViewModel
import com.lebogang.vibe.ui.local.viewmodel.GenreViewModel
import com.lebogang.vibe.ui.player.PlayerActivity
import com.lebogang.vibe.ui.stream.StreamActivity
import com.lebogang.vibe.ui.utils.*
import com.lebogang.vibe.utils.Keys

class HomeActivity : AppCompatActivity() {
    private val app:VibeApplication by lazy { application as VibeApplication }
    private val bind: ActivityHomeBinding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val albumViewModel: AlbumViewModel by lazy { ModelFactory(app).getAlbumViewModel()}
    private val artistViewModel:ArtistViewModel by lazy { ModelFactory(app).getArtistViewModel()}
    private val genreViewModel:GenreViewModel by lazy { ModelFactory(app).getGenreViewModel()}
    private val localContent:LocalContent by lazy { app.localContent }
    private val albumAdapter = AlbumPreviewAdapter()
    private val artistAdapter = ArtistPreviewAdapter()
    private val genreAdapter = GenrePreviewAdapter()
    private val contract: ActivityResultLauncher<String> by lazy{
        registerForActivityResult(RequestPermission()) { if (it) getData() else showPermissionErrorDialog() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.app_name)
        setContentView(bind.root)
        setSupportActionBar(bind.toolbar)
        requirePermission()
        albumViewModel.registerObserver(app)
    }

    private fun requirePermission(){
        if (checkCallingPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            getData()
        else
            contract.launch(READ_EXTERNAL_STORAGE)
    }

    private fun showPermissionErrorDialog() = MaterialAlertDialogBuilder(this)
        .setBackground(DialogStyle.getDialogBackground(this))
        .setTitle(getString(R.string.permission_error))
        .setMessage(getString(R.string.permission_error_message))
        .setPositiveButton(getString(R.string.grant)) { _, _ -> contract.launch(READ_EXTERNAL_STORAGE) }
        .setNegativeButton(getString(R.string.close)){_,_-> finish()}
        .create().show()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.application_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.search ->{
                startActivity(Intent(this,SearchActivity::class.java))
                true
            }
            R.id.refresh->{
                bind.progressBar.visibility = View.VISIBLE
                localContent.reset()
                true
            }
            else-> false
        }
    }

    private fun getData(){
        localContent.initDatabase()
        initAlbums()
        initArtists()
        initGenres()
        initViews()
    }

    private fun initViews(){
        bind.albumsView.setOnClickListener { startActivity(Intent(this,
            AlbumsActivity::class.java)) }
        bind.artistsView.setOnClickListener { startActivity(Intent(this,
            ArtistsActivity::class.java)) }
        bind.genresView.setOnClickListener { startActivity(Intent(this,
            GenresActivity::class.java))}
        bind.playlistView.setOnClickListener { startActivity(Intent(this,
            PlaylistActivity::class.java))}
        bind.favouriteView.setOnClickListener { startActivity(Intent(this,
            FavouriteActivity::class.java)) }
        bind.songsView.setOnClickListener { startActivity(Intent(this,
            MusicActivity::class.java)) }
        bind.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.stream -> startActivity(Intent(this, StreamActivity::class.java))
                R.id.settings-> startActivity(Intent(this,SettingsActivity::class.java))
                R.id.charts-> startActivity(Intent(this,ChartsActivity::class.java))
            }
            false
        }
        bind.nowPlaying.setOnClickListener { startActivity(Intent(this,
            PlayerActivity::class.java)) }
        bind.historyView.setOnClickListener { startActivity(Intent(this,
            HistoryActivity::class.java)) }
    }

    private fun initAlbums(){
        //get album data
        albumViewModel.getAlbumsPreview().observe(this,{ albumAdapter.setData(it) })
        //recycler view for albums
        albumAdapter.imageLoader = ImageLoader(this)
        albumAdapter.itemClickInterface = getAlbumItemClickInterface()
        bind.recyclerViewAlbums.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL, false)
        bind.recyclerViewAlbums.adapter = albumAdapter
    }

    private fun getAlbumItemClickInterface() = object : ItemClickInterface {
        override fun onItemClick(view: View, item: Any?, type: Type) {
            when (item) {
                null -> {
                    startActivity(Intent(this@HomeActivity, AlbumsActivity::class.java))
                }
                else -> {
                    val album = item as Album
                    val intent = Intent(this@HomeActivity,AlbumDetailsActivity::class.java)
                    intent.putExtra(Keys.ALBUM_KEY,album.id)
                    startActivity(intent)
                }
            }
        }
    }

    private fun initArtists(){
        //get artist data
        artistViewModel.getArtistsPreview().observe(this,{ artistAdapter.setData(it) })
        //recycler view for artists
        artistAdapter.imageLoader = ImageLoader(this)
        artistAdapter.itemClickInterface = getArtistItemClickInterface()
        bind.recyclerViewArtists.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL, false)
        bind.recyclerViewArtists.adapter = artistAdapter
    }

    private fun getArtistItemClickInterface() = object : ItemClickInterface {
        override fun onItemClick(view: View, item: Any?, type: Type) {
            when (item) {
                null -> startActivity(Intent(this@HomeActivity,
                    ArtistsActivity::class.java))
                else -> {
                    val artist = item as Artist
                    val intent = Intent(this@HomeActivity,ArtistDetailsActivity::class.java)
                    intent.putExtra(Keys.ARTIST_KEY,artist.id)
                    startActivity(intent)
                }
            }
        }
    }

    private fun initGenres(){
        //get genre data
        genreViewModel.getGenrePreview().observe(this,{
            genreAdapter.setData(it)
            bind.progressBar.visibility = View.GONE
        })
        //recycler view for genres
        genreAdapter.itemClickInterface = geGenreItemClickInterface()
        bind.recyclerViewGenre.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL, false)
        bind.recyclerViewGenre.adapter = genreAdapter
        //view more genres
        bind.moreGenresButton.setOnClickListener { startActivity(Intent(this,
            GenresActivity::class.java))}
    }

    private fun geGenreItemClickInterface() = object : ItemClickInterface {
        override fun onItemClick(view: View, item: Any?, type: Type) {
            val genre = item as Genre
            val intent = Intent(this@HomeActivity,GenreDetailsActivity::class.java)
            intent.putExtra(Keys.GENRE_KEY,genre)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        albumViewModel.unregisterObserver(app)
    }

}
