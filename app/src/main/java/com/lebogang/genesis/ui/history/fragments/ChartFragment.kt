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

package com.lebogang.genesis.ui.history.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lebogang.genesis.GenesisApplication
import com.lebogang.genesis.database.history.models.History
import com.lebogang.genesis.databinding.FragmentHistoryBinding
import com.lebogang.genesis.ui.ImageLoader
import com.lebogang.genesis.ui.ItemClickInterface
import com.lebogang.genesis.ui.ModelFactory
import com.lebogang.genesis.ui.Type
import com.lebogang.genesis.ui.history.adapters.MusicAdapter
import com.lebogang.genesis.ui.history.dialogs.HistoryDialog
import com.lebogang.genesis.ui.history.viewmodels.HistoryViewModel

class ChartFragment: Fragment() {
    private lateinit var bind:FragmentHistoryBinding
    private val app:GenesisApplication by lazy{requireActivity().application as GenesisApplication}
    private val viewModel:HistoryViewModel by lazy { ModelFactory(app).getHistoryViewModel() }
    private val adapter = MusicAdapter()

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View {
        bind = FragmentHistoryBinding.inflate(layoutInflater, parent, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView(){
        adapter.imageLoader = ImageLoader(requireActivity())
        adapter.itemClickInterface = getItemClickInterface()
        viewModel.getChartHistory().observe(viewLifecycleOwner,{
            adapter.setData(it)
        })
        bind.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        bind.recyclerView.adapter = adapter
    }

    private fun getItemClickInterface() = object:ItemClickInterface{
        override fun onItemClick(view: View, item: Any?, type: Type) {
            val history = item as History
            val historyDialog = HistoryDialog()
            historyDialog.history = history
            historyDialog.showNow(requireActivity().supportFragmentManager,null)
        }
    }
}
