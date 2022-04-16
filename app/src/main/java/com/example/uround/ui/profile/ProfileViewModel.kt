package com.example.uround.ui.profile

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is profile Fragment"
    }
    val text: LiveData<String> = _text
}