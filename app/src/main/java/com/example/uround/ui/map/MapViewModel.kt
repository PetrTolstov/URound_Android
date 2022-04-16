package com.example.uround.ui.map

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class MapViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is map Fragment"
    }
    val text: LiveData<String> = _text
}