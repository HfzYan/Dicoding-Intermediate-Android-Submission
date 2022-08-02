package com.example.storyappdicoding.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.storyappdicoding.response.ListStoryItem

class StoryDiffCallback (private val mOldStoryList: List<ListStoryItem>, private val mNewStoryList: List<ListStoryItem>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldStoryList.size
    }

    override fun getNewListSize(): Int {
        return mNewStoryList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldStoryList[oldItemPosition].id == mNewStoryList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = mOldStoryList[oldItemPosition]
        val newUser = mNewStoryList[newItemPosition]
        return oldUser.id == newUser.id
    }
}