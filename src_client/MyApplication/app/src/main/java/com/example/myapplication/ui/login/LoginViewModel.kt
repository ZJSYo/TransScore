package com.example.myapplication.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.myapplication.logic.UserRepository
import com.example.myapplication.logic.model.User

class LoginViewModel: ViewModel() {
    private var usernameLiveData = MutableLiveData<String>()
    val userLiveData = usernameLiveData.switchMap {
        username -> UserRepository.findUser(username)
    }
    fun findUser(username: String) {
        usernameLiveData.value = username
    }
}