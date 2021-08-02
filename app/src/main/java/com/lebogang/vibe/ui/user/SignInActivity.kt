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

package com.lebogang.vibe.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.lebogang.vibe.R
import com.lebogang.vibe.databinding.ActivitySignInBinding

class SignInActivity : GoogleHelper() {
    private val bind:ActivitySignInBinding by lazy{ActivitySignInBinding.inflate(layoutInflater)}
    private val auth:FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = null
        setContentView(bind.root)
        initToolbar()
        initViewPager()
        initViews()
    }

    private fun initToolbar(){
        setSupportActionBar(bind.toolbar)
        bind.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initViewPager(){
        bind.viewpager.adapter = ViewpagerAdapter(this)
        TabLayoutMediator(bind.tabLayout,bind.viewpager){tab,position->
            when(position){
                0 -> tab.text = getString(R.string.login_in)
                1 -> tab.text = getString(R.string.create_new)
            }
        }.attach()
    }

    private fun initViews(){
        bind.googleButton.setOnClickListener { super.signIn() }
        bind.phoneButton.setOnClickListener { startActivity(Intent(this,PhoneActivity::class.java)) }
    }

    internal fun signInEmail(email:String,password:String){
        bind.progressBar.visibility = View.VISIBLE
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
            bind.progressBar.visibility = View.GONE
            if (it.isSuccessful)
                startActivity(Intent(this,UserActivity::class.java))
            else
                Toast.makeText(this,getString(R.string.check_your_details),Toast.LENGTH_SHORT).show()
        }
    }

    internal fun createUser(email:String,password:String,username:String){
        bind.progressBar.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful)
                addUserName(username)
            else {
                bind.progressBar.visibility = View.GONE
                Toast.makeText(this, getString(R.string.check_your_details), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun addUserName(username: String){
        val user = UserProfileChangeRequest.Builder()
            .setDisplayName(username)
            .build()
        auth.currentUser?.updateProfile(user)?.addOnCompleteListener {
            bind.progressBar.visibility = View.GONE
            //go somewhere
        }
    }

}
