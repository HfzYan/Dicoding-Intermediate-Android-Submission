package com.example.storyappdicoding.router

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyappdicoding.helper.UserPreferences
import com.example.storyappdicoding.helper.ViewModelFactory
import com.example.storyappdicoding.ui.login.LoginActivity
import com.example.storyappdicoding.ui.main.MainActivity
import com.example.storyappdicoding.ui.main.MainViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class RouterActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_router)

        startRouter()
    }

    private fun startRouter() {
        mainViewModel = ViewModelProvider(this, ViewModelFactory(UserPreferences.getInstance(dataStore)))[MainViewModel::class.java]
        mainViewModel.getUser().observe(this){
            if(it.isLogin){
                startActivity(Intent(this@RouterActivity, MainActivity::class.java))
                finish()
            }
            else{
                startActivity(Intent(this@RouterActivity, LoginActivity::class.java))
                finish()
            }
        }
    }
}