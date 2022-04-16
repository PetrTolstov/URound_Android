package com.example.uround.ui.messages

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class MessagesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is messages Fragment"
    }
    val text: LiveData<String> = _text
}