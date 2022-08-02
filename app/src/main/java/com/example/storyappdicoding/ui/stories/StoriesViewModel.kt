package com.example.storyappdicoding.ui.stories

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyappdicoding.api.ApiConfig
import com.example.storyappdicoding.response.GetStoriesResponse
import com.example.storyappdicoding.response.ListStoryItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoriesViewModel: ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isDataFetched = MutableLiveData<Boolean>()
    val isDataFetched: LiveData<Boolean> = _isDataFetched

    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    val listStory: LiveData<List<ListStoryItem>> = _listStory

    interface Status{
        fun isSuccess(status: Boolean, message: String)
    }

    fun getStoryList(token: String, status: Status){
        _isLoading.value = true
        _isDataFetched.value = false
        val client = ApiConfig.getApiService().getAllStories("Bearer $token")
        client.enqueue(object : Callback<GetStoriesResponse> {
            override fun onResponse(
                call: Call<GetStoriesResponse>,
                response: Response<GetStoriesResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {

                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listStory.value = response.body()?.listStory
                        _isDataFetched.value = true
                        status.isSuccess(true, "")
                    }
                    else{
                        Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                        status.isSuccess(false, "Empty")
                    }
                }
                else{
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    status.isSuccess(false, "Failed")
                }
            }
            override fun onFailure(call: Call<GetStoriesResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                status.isSuccess(false, "Failed")

            }

        })
    }

}