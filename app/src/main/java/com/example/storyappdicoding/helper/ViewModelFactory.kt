package com.example.storyappdicoding.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyappdicoding.ui.login.LoginViewModel
import com.example.storyappdicoding.ui.main.MainViewModel


class ViewModelFactory(private val mPref: UserPreferences) : ViewModelProvider.NewInstanceFactory() {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(mPref) as T
        }
        else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(mPref) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}