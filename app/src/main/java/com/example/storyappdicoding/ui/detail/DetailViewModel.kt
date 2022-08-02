package com.example.storyappdicoding.ui.detail

import androidx.lifecycle.ViewModel
import com.example.storyappdicoding.response.ListStoryItem

class DetailViewModel: ViewModel() {
    lateinit var itemStory: ListStoryItem

    fun setStoryData(story: ListStoryItem): ListStoryItem{
        itemStory = story
        return itemStory
    }

}