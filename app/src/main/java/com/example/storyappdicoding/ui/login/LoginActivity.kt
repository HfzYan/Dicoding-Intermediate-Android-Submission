package com.example.storyappdicoding.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyappdicoding.R
import com.example.storyappdicoding.databinding.ActivityLoginBinding
import com.example.storyappdicoding.helper.UserPreferences
import com.example.storyappdicoding.helper.ViewModelFactory
import com.example.storyappdicoding.ui.main.MainActivity
import com.example.storyappdicoding.ui.register.RegisterActivity
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //create viewmodel
        loginViewModel = ViewModelProvider(this, ViewModelFactory(UserPreferences.getInstance(dataStore)))[LoginViewModel::class.java]
        //observe progress bar
        loginViewModel.isLoading.observe(this){
            showLoading(it)
        }

        //check button status
        setEnableButton()
        setLanguageInfo()

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setEnableButton()
                hideErrorText()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setEnableButton()
                hideErrorText()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        //register listener
        binding.etRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }

        //button listener
        binding.loginButton.setOnClickListener{
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            loginViewModel.userLogin(email, password, object : LoginViewModel.Status{

                override fun isSuccess(status: Boolean, statusString: String?) {
                    if(status){
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    else if(!status && (statusString == "Unauthorized" || statusString == "Bad Request")){
                        showErrorText(true)
                    }
                    else{
                        showErrorText(false)
                    }
                }
            })
        }
        binding.imgLanguage.setOnClickListener{
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun setLanguageInfo() {
        binding.tvLanguage.text = Locale.getDefault().displayLanguage
    }

    private fun setEnableButton(){
        val formEmail = binding.etEmail.text
        val formPassword = binding.etPassword.text
        val loginButton = binding.loginButton

        loginButton.isEnabled = formEmail != null &&
                formPassword != null &&
                Patterns.EMAIL_ADDRESS.matcher(formEmail).matches() &&
                formPassword.toString().length >= 6
    }

    private fun showLoading(isLoading: Boolean){
        if(isLoading){
            binding.loginProgressBar.visibility = View.VISIBLE
        }
        else{
            binding.loginProgressBar.visibility = View.INVISIBLE
        }
    }

    private fun showErrorText(isWrongEmailPass: Boolean){
        binding.tvError.visibility = View.VISIBLE
        if(isWrongEmailPass){ binding.tvError.text = getString(R.string.wrong_emailpass_login)}
        else{binding.tvError.text = getString(R.string.failed_login)}
    }

    private fun hideErrorText(){
        binding.tvError.visibility = View.INVISIBLE
    }
}