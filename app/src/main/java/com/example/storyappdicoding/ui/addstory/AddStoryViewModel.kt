package com.example.storyappdicoding.ui.addstory

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyappdicoding.api.ApiConfig
import com.example.storyappdicoding.response.RegisterAddStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddStoryViewModel: ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    interface Status{
        fun isSuccess(status: Boolean)
    }

    fun uploadImage(token: String,
                    description: RequestBody,
                    imageMultipart: MultipartBody.Part,
                    status: Status
    ){
        _isLoading.value = true
        val client = ApiConfig.getApiService().addStory("Bearer $token", description, imageMultipart)
        client.enqueue(object : Callback<RegisterAddStoryResponse> {
            override fun onResponse(
                call: Call<RegisterAddStoryResponse>,
                response: Response<RegisterAddStoryResponse>
            ) {
                if(response.isSuccessful){
                    _isLoading.value = false
                    val responseBody = response.body()
                    if(responseBody!=null){
                        status.isSuccess(true)
                    }
                    else{
                        _isLoading.value = false
                        Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                        status.isSuccess(false)
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
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                status.isSuccess(false)
            }

        })
    }

}