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

package com.lebogang.genesis.ui.user

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.lebogang.genesis.R
import com.lebogang.genesis.databinding.ActivityPhoneBinding
import com.lebogang.genesis.ui.DialogStyle
import java.util.concurrent.TimeUnit

class PhoneActivity : GoogleHelper() {
    private val bind:ActivityPhoneBinding by lazy {ActivityPhoneBinding.inflate(layoutInflater)}
    private val auth:FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private var verificationId:String = ""
    private val countDownTimer = object :CountDownTimer(60000,1000){
        override fun onTick(millisUntilFinished: Long) {
            val time = (millisUntilFinished/1000).toString()
            bind.counterTextView.text = time
        }
        override fun onFinish() {
            bind.counterTextView.text = getString(R.string._01_00)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.phone)
        setContentView(bind.root)
        initToolbar()
        initViews()
    }

    private fun initToolbar(){
        setSupportActionBar(bind.toolbar)
        bind.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initViews(){
        bind.googleButton.setOnClickListener { signIn() }
        bind.emailButton.setOnClickListener { startActivity(Intent(this,SignInActivity::class.java)) }

        bind.ccp.setOnCountryChangeListener {
            val text = "+" + bind.ccp.selectedCountryCode + bind.phoneEditText.text?.toString()
            bind.phoneEditText.setText(text)
        }
        bind.phoneEditText.doAfterTextChanged {
            bind.requestCodeButton.isEnabled = !it.isNullOrEmpty()
        }
        bind.requestCodeButton.setOnClickListener {
            bind.progressBar.visibility = View.VISIBLE
            bind.phoneEditText.text?.let {
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(it.toString())
                    .setTimeout(60L,TimeUnit.SECONDS)
                    .setActivity(this)
                    .setCallbacks(getPhoneCallbacks())
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            }
        }

        bind.codeEditText.doAfterTextChanged {
            bind.signButton.isEnabled = !bind.codeEditText.text.isNullOrEmpty()
        }

        bind.signButton.setOnClickListener {
            bind.codeEditText.text?.let {
                val credential = PhoneAuthProvider.getCredential(verificationId, it.toString())
                signIn(credential)
            }
        }
    }

    private fun getPhoneCallbacks() = object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onCodeAutoRetrievalTimeOut(p0: String) {
            super.onCodeAutoRetrievalTimeOut(p0)
            bind.progressBar.visibility = View.GONE
            countDownTimer.onFinish()
            showErrorDialog()
        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            bind.progressBar.visibility = View.GONE
            bind.codeEditText.isEnabled = true
            countDownTimer.start()
            verificationId = p0
        }

        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            bind.progressBar.visibility = View.GONE
            countDownTimer.onFinish()
            signIn(p0)
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            bind.progressBar.visibility = View.GONE
            countDownTimer.onFinish()
            showErrorDialog()
        }
    }

    private fun showErrorDialog() = MaterialAlertDialogBuilder(this)
        .setBackground(DialogStyle.getDialogBackground(this))
        .setTitle(getString(R.string.verification_error))
        .setMessage(getString(R.string.verify_number_error_message))
        .setPositiveButton(getString(R.string.okay),null)
        .create().show()

    private fun signIn(credential: PhoneAuthCredential){
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                //do something here
            }else
                showInvalidCodeDialog()
        }
    }

    private fun showInvalidCodeDialog()= MaterialAlertDialogBuilder(this)
        .setBackground(DialogStyle.getDialogBackground(this))
        .setTitle(getString(R.string.verification_error))
        .setMessage(getString(R.string.invalid_code))
        .setPositiveButton(getString(R.string.okay),null)
        .create().show()
}
