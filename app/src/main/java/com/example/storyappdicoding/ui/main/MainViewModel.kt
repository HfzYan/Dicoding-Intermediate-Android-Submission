package com.example.storyappdicoding.ui.main

import androidx.lifecycle.*
import com.example.storyappdicoding.helper.User
import com.example.storyappdicoding.helper.UserPreferences
import kotlinx.coroutines.launch



class MainViewModel(private val pref: UserPreferences): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLogout = MutableLiveData<Boolean>()
    val isLogout: LiveData<Boolean> = _isLogout


    fun getUser(): LiveData<User>{
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch{
            _isLoading.value = true
            _isLogout.value = false
            _isLogout.value = true
            pref.logout()
            _isLoading.value = false
        }
    }
}