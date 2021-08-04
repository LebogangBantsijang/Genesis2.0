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

package com.lebogang.vibe.ui.fragments.local

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lebogang.vibe.R
import com.lebogang.vibe.data.models.Album
import com.lebogang.vibe.databinding.FragmentAlbumsBinding
import com.lebogang.vibe.settings.ThemeSettings
import com.lebogang.vibe.ui.adapters.ItemAlbumAdapter
import com.lebogang.vibe.ui.adapters.utils.OnAlbumClickListener
import com.lebogang.vibe.utils.Keys
import com.lebogang.vibe.viewmodels.AlbumViewModel
import com.lebogang.vibe.viewmodels.ViewModelFactory

class AlbumsFragment: Fragment(), OnAlbumClickListener {
    private val viewBinding: FragmentAlbumsBinding by lazy{ FragmentAlbumsBinding.inflate(layoutInflater) }
    private val adapter = ItemAlbumAdapter().apply { listener = this@AlbumsFragment }
    private val albumViewModel: AlbumViewModel by lazy {
        ViewModelFactory(requireActivity().application).getAlbumViewModel() }
    private val themeSettings: ThemeSettings by lazy { ThemeSettings(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclingView()
        initSearchView()
        requestPermission()
    }

    /**
     * Seeing that this is the home fragment, check if write permissions are granted
     * */
    private fun requestPermission(){
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            populateView()
        }else
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 25)
    }

    private fun initRecyclingView(){
        viewBinding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(themeSettings.getColumnCount(), StaggeredGridLayoutManager.VERTICAL)
        viewBinding.recyclerView.adapter = adapter
    }

    private fun populateView(){
        albumViewModel.getAlbums()
        albumViewModel.registerContentObserver()
        albumViewModel.liveData.observe(viewLifecycleOwner,{
            adapter.setAlbumData(it)
            loadingView(it.isNotEmpty())
            val count = getString(R.string.total) + " " + it.size.toString()
            viewBinding.counterView.text = count
        })
    }

    /**
     * Create search view for albums
     * */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.column_view_menu, menu)
    }

    /**
     * Handle album options: mainly columns
     * */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.two_column ->{
                themeSettings.setColumnCount(2)
                (viewBinding.recyclerView.layoutManager as StaggeredGridLayoutManager).spanCount = 2
                true
            }
            R.id.three_column ->{
                themeSettings.setColumnCount(3)
                (viewBinding.recyclerView.layoutManager as StaggeredGridLayoutManager).spanCount = 3
                true
            }
            R.id.search -> {
                showHideSearchView(true)
                true
            }
            else ->super.onOptionsItemSelected(item)
        }
    }

    private fun showHideSearchView(show:Boolean){
        if (show){
            viewBinding.searchContainerView.visibility = View.VISIBLE
            viewBinding.searchContainerView.animate().alpha(1f).setDuration(1500)
                .withEndAction { viewBinding.searchView.requestFocus() }.start()
        }else{
            viewBinding.searchContainerView.animate().alpha(0f).setDuration(1000)
                .withEndAction {
                    viewBinding.recyclerView.requestFocus()
                    viewBinding.searchContainerView.visibility = View.GONE
                }.start()
        }
    }

    private fun initSearchView(){
        viewBinding.closeSearchView.setOnClickListener { showHideSearchView(false) }
    }

    /**
     * Hide or show loading view
     * */
    private fun loadingView(hasContent:Boolean){
        viewBinding.loadingView.visibility = View.GONE
        if (hasContent){
            viewBinding.noContentView.text = null
        }else{
            viewBinding.noContentView.text = getString(R.string.no_content)
        }
    }

    /**
     * Remove content observers
     * */
    override fun onDestroy() {
        super.onDestroy()
        albumViewModel.unregisterContentContentObserver()
    }

    /**
     * Navigate to album view fragment
     * @param album: album to view
     * @param imageView: Planning to use it for that shared animations thing, forgot the name
     * */
    override fun onAlbumClick(album: Album, imageView: View) {
        val bundle = Bundle().apply{putParcelable(Keys.ALBUM_KEY, album)}
        val controller = findNavController()
        controller.navigate(R.id.viewAlbumFragment, bundle)
    }

}
