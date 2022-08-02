package com.example.storyappdicoding.ui.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.storyappdicoding.R
import com.example.storyappdicoding.databinding.ActivityRegisterBinding
import com.example.storyappdicoding.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private val registerViewModel: RegisterViewModel by viewModels()
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerViewModel.isLoading.observe(this){
            showLoading(it)
        }

        //check button status
        setEnableButton()

        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setEnableButton()
                hideErrorText()

            }

            override fun afterTextChanged(s: Editable) {
            }
        })

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
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }

        //button listener
        binding.registerButton.setOnClickListener{
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            registerViewModel.userRegister(name, email, password, object : RegisterViewModel.Status {
                override fun isSuccess(status: Boolean) {
                    if(!status){
                        showErrorText()
                    }
                    else {
                        AlertDialog.Builder(this@RegisterActivity).apply {
                            setTitle(getString(R.string.information))
                            setMessage(getString(R.string.success_register))
                            setPositiveButton(getString(R.string.back_to_login)) { _, _ ->
                                val intent =
                                    Intent(this@RegisterActivity, LoginActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                }

            })
        }
    }

    private fun setEnableButton(){
        val formName = binding.etName.text
        val formEmail = binding.etEmail.text
        val formPassword = binding.etPassword.text
        val registerButton = binding.registerButton

        registerButton.isEnabled = formName != null &&
                formEmail != null &&
                formPassword != null &&
                Patterns.EMAIL_ADDRESS.matcher(formEmail).matches() &&
                formPassword.toString().length >= 6
    }

    private fun showLoading(isLoading: Boolean){
        if(isLoading){
            binding.registerProgressBar.visibility = View.VISIBLE
        }
        else{
            binding.registerProgressBar.visibility = View.INVISIBLE
        }
    }

    private fun showErrorText(){
        binding.tvError.visibility = View.VISIBLE
    }

    private fun hideErrorText(){
        binding.tvError.visibility = View.INVISIBLE
    }
}
