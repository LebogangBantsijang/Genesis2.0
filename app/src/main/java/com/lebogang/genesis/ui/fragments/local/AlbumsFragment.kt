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

package com.lebogang.genesis.ui.fragments.local

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lebogang.genesis.R
import com.lebogang.genesis.data.models.Album
import com.lebogang.genesis.databinding.FragmentAlbumsBinding
import com.lebogang.genesis.settings.ThemeSettings
import com.lebogang.genesis.ui.adapters.ItemAlbumAdapter
import com.lebogang.genesis.ui.adapters.utils.OnAlbumClickListener
import com.lebogang.genesis.utils.Keys
import com.lebogang.genesis.viewmodels.AlbumViewModel
import com.lebogang.genesis.viewmodels.ViewModelFactory

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
        viewBinding.recyclerView.layoutManager = StaggeredGridLayoutManager(themeSettings.getColumnCount(), StaggeredGridLayoutManager.VERTICAL)
        viewBinding.recyclerView.adapter = adapter
        albumViewModel.liveData.observe(viewLifecycleOwner,{
            adapter.setAlbumData(it)
            loadingView(it.isNotEmpty())
            val count = getString(R.string.total) + " " + it.size.toString()
            viewBinding.counterView.text = count
        })
        albumViewModel.getAlbums()
        albumViewModel.registerContentObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.column_view_menu, menu)
        val searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }

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
            else ->super.onOptionsItemSelected(item)
        }
    }

    private fun loadingView(hasContent:Boolean){
        viewBinding.loadingView.visibility = View.GONE
        if (hasContent){
            viewBinding.noContentView.text = null
        }else{
            viewBinding.noContentView.text = getString(R.string.no_content)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        albumViewModel.unregisterContentContentObserver()
    }

    override fun onAlbumClick(album: Album, imageView: View) {
        val bundle = Bundle().apply{putParcelable(Keys.ALBUM_KEY, album)}
        val controller = findNavController()
        controller.navigate(R.id.viewAlbumFragment, bundle)
    }

}