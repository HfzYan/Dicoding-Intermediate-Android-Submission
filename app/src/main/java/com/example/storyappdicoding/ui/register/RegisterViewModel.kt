package com.example.storyappdicoding.ui.register

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyappdicoding.api.ApiConfig
import com.example.storyappdicoding.response.RegisterAddStoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel: ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    interface Status{
        fun isSuccess(status: Boolean)
    }

    fun userRegister(name: String, email: String, password: String, status: Status){
        _isLoading.value = true
        val client = ApiConfig.getApiService().register(name, email, password)
        client.enqueue(object : Callback<RegisterAddStoryResponse> {
            override fun onResponse(
                call: Call<RegisterAddStoryResponse>,
                response: Response<RegisterAddStoryResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()?.error
                    if (responseBody != null && responseBody != false) {
                        status.isSuccess(true)
                    } else {
                        status.isSuccess(true)
                        Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    }
                }
                else{
                    _isLoading.value = false
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    status.isSuccess(false)
                }
            }

            override fun onFailure(call: Call<RegisterAddStoryResponse>, t: Throwable) {
                _isLoading.value = false
                status.isSuccess(false)
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }
    }
