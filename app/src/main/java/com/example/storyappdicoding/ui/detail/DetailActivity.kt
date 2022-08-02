package com.example.storyappdicoding.ui.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storyappdicoding.R
import com.example.storyappdicoding.databinding.ActivityDetailBinding
import com.example.storyappdicoding.response.ListStoryItem

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var story: ListStoryItem

    private val detailViewModel: DetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.story)

        story = intent.getParcelableExtra(EXTRA_STORY)!!

        detailViewModel.setStoryData(story)

        with(binding){
            val item = detailViewModel.itemStory
            tvUsername.text = item.name
            tvDescription.text =item.description
            Glide.with(ivStory)
                .load(item.photoUrl)
                .placeholder(R.drawable.ic_baseline_broken_image_24)
                .into(ivStory)

        }



    }

    companion object{
        const val EXTRA_STORY = "story"
    }
}