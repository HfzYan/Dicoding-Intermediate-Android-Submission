package com.example.storyappdicoding.ui.stories

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyappdicoding.R
import com.example.storyappdicoding.databinding.ActivityStoriesBinding
import com.example.storyappdicoding.helper.User
import com.example.storyappdicoding.ui.addstory.AddStoryActivity
import com.example.storyappdicoding.ui.stories.adapter.StoriesAdapter

class StoriesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoriesBinding
    private lateinit var user: User
    private lateinit var adapter: StoriesAdapter
    private val storiesViewModel: StoriesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.story)

        user = intent.getParcelableExtra(EXTRA_USER)!!

        setStoryList()
        adapter = StoriesAdapter()

        setRecyclerList()

        //observe progress bar
        storiesViewModel.isLoading.observe(this){
            showLoading(it)
        }

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this@StoriesActivity, AddStoryActivity::class.java)
            intent.putExtra(AddStoryActivity.EXTRA_USER, user)
            startActivity(intent)
        }

        setRecyclerVisibilty()


    }

    private fun setRecyclerVisibilty() {
        storiesViewModel.isDataFetched.observe(this){
            binding.rvStory.visibility = if(it) View.VISIBLE else View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        //setStoryList()
    }

    private fun setRecyclerList() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        binding.rvStory.setHasFixedSize(true)
        binding.rvStory.adapter = adapter

    }


    private fun setStoryList() {
        storiesViewModel.getStoryList(user.token, object : StoriesViewModel.Status {
            override fun isSuccess(status: Boolean, message: String) {
                if(!status){
                    binding.rvStory.visibility = View.INVISIBLE
                    if(message == "Empty"){
                        binding.tvNotice.apply {
                            visibility = View.VISIBLE
                            text = context.getString(R.string.notice_no_story)
                        }
                    }
                    else if(message == "Failed"){
                        binding.tvNotice.apply {
                            visibility = View.VISIBLE
                            text = context.getString(R.string.notice_error_story)
                        }
                    }
                    
                }
 
            }

        })
        storiesViewModel.listStory.observe(this){
            adapter.setListStory(it)
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_settings -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            else ->{
                super.onOptionsItemSelected(item)
            }
        }
    }


    private fun showLoading(isLoading: Boolean){
        if(isLoading){
            binding.storiesProgressBar.visibility = View.VISIBLE
        }
        else{
            binding.storiesProgressBar.visibility = View.INVISIBLE
        }
    }

    companion object{
        const val EXTRA_USER = "user"
    }
}


