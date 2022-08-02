package com.example.storyappdicoding.ui.main

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyappdicoding.R
import com.example.storyappdicoding.databinding.ActivityMainBinding
import com.example.storyappdicoding.helper.User
import com.example.storyappdicoding.helper.UserPreferences
import com.example.storyappdicoding.helper.ViewModelFactory
import com.example.storyappdicoding.ui.login.LoginActivity
import com.example.storyappdicoding.ui.stories.StoriesActivity



private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()

        mainViewModel.isLoading.observe(this){
            showLoading(it)
        }

        mainViewModel.isLogout.observe(this){
            if(it) {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }

        setupSavedUser()
        playAnimation()
        buttonListener()
    }

    private fun buttonListener() {
        binding.startButton.setOnClickListener{
            val intent = Intent(this@MainActivity, StoriesActivity::class.java)
            intent.putExtra(StoriesActivity.EXTRA_USER, user)
            startActivity(intent)
        }
        binding.logoutButton.setOnClickListener{
            binding.tvWelcome.visibility = View.INVISIBLE
            mainViewModel.logout()
        }
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(this, ViewModelFactory(UserPreferences.getInstance(dataStore)))[MainViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        if(isLoading){
            binding.mainProgressBar.visibility = View.VISIBLE
        }
        else{
            binding.mainProgressBar.visibility = View.INVISIBLE
        }
    }

    private fun setupSavedUser() {
        mainViewModel.getUser().observe(this){
            user = User(
                it.name,
                it.email,
                it.password,
                it.userId,
                it.token,
                it.isLogin
            )
            binding.tvWelcome.text = getString(R.string.welcome_login, user.name)
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply{
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        ObjectAnimator.ofFloat(binding.tvWelcome, View.TRANSLATION_X, -300f, 0f).apply{
            duration = 1000
        }.start()
        ObjectAnimator.ofFloat(binding.startButton, View.TRANSLATION_X, -300f, 0f).apply{
            duration = 1200
        }.start()
        ObjectAnimator.ofFloat(binding.logoutButton, View.TRANSLATION_X, -300f, 0f).apply{
            duration = 1300
        }.start()

    }
}