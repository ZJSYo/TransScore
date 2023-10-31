package com.example.myapplication.ui.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InfoViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "识音1.0\n团队成员介绍\n张锦深\n"//
    }
    val text: LiveData<String> = _text
}