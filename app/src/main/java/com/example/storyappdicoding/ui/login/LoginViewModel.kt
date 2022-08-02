package com.example.storyappdicoding.ui.login

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyappdicoding.api.ApiConfig
import com.example.storyappdicoding.helper.User
import com.example.storyappdicoding.helper.UserPreferences
import com.example.storyappdicoding.response.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreferences): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    interface Status{
        fun isSuccess(status: Boolean, statusString: String ?= "")
    }

    fun userLogin(email: String, password: String, status: Status ){
        _isLoading.value = true
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if(response.isSuccessful){
                    _isLoading.value = false
                    val responseBody = response.body()?.loginResult
                    if(responseBody != null){
                        val userModel = User(
                            responseBody.name,
                            email,
                            password,
                            responseBody.userId,
                            responseBody.token,
                            true
                        )
                        viewModelScope.launch {
                            pref.saveUser(userModel)
                        }
                        status.isSuccess(true)
                    }
                    else{
                        _isLoading.value = false
                        Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                        status.isSuccess(false, response.message())
                    }

                }
                else{
                    _isLoading.value = false
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    status.isSuccess(false, response.message())
                }

            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                status.isSuccess(false)
            }

        })
    }
}