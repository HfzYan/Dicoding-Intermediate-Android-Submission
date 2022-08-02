package com.example.storyappdicoding.ui.stories.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyappdicoding.R
import com.example.storyappdicoding.databinding.ItemRowStoryBinding
import com.example.storyappdicoding.helper.StoryDiffCallback
import com.example.storyappdicoding.response.ListStoryItem
import com.example.storyappdicoding.ui.detail.DetailActivity
import androidx.core.util.Pair

class StoriesAdapter : RecyclerView.Adapter<StoriesAdapter.StoryViewHolder>() {
    private lateinit var binding: ItemRowStoryBinding
    private val listStory = ArrayList<ListStoryItem>()

    fun setListStory(mListStory: List<ListStoryItem>) {
        val diffCallback = StoryDiffCallback(listStory, mListStory)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listStory.clear()
        listStory.addAll(mListStory)
        diffResult.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        binding = ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    override fun getItemCount(): Int = listStory.size

    inner class StoryViewHolder(binding: ItemRowStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            with(binding) {
                tvUsername.text = story.name
                tvDescription.text = story.description
                Glide.with(imgStory)
                    .load(story.photoUrl)
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .into(imgStory)
                cvStory.setOnClickListener {
                    val intent = Intent(it.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_STORY, story)
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(imgStory, "image"),
                            Pair(tvUsername, "name"),
                            Pair(tvDescription, "description")
                        )
                    it.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }

    }
}

