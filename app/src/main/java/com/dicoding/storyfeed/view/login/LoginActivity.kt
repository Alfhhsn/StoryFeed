package com.dicoding.storyfeed.view.login

import android.content.Intent
import androidx.activity.viewModels
import com.dicoding.storyfeed.databinding.ActivityLoginBinding
import com.dicoding.storyfeed.di.ResultState
import com.dicoding.storyfeed.util.showError
import com.dicoding.storyfeed.view.ViewModelFactory
import com.dicoding.storyfeed.view.base.BaseActivity
import com.dicoding.storyfeed.view.main.MainActivity
import com.dicoding.storyfeed.view.signup.SignupActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun getViewBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun initUI() {
        binding.loginButton.setOnClickListener {
            login()
        }
        binding.signupButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    override fun initProcess() {

    }

    override fun initObservers() {

    }

    private fun login() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        when {
            email.isEmpty() -> {
                binding.emailEditText.showError("")
            }

            password.isEmpty() -> {
                binding.passwordEditText.showError("")
            }

            password.length < 8 -> {
                binding.passwordEditText.showError("")
            }

            else -> {
                loginViewModel.loginUser(email, password).observe(this) { response ->
                    when (response) {
                        is ResultState.Error -> {
                            closeLoadingDialog()
                            showToast("error")
                        }

                        ResultState.Loading -> {
                            showLoadingDialog()
                        }

                        is ResultState.Success -> {
                            closeLoadingDialog()
                            showToast("Success Login")
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

}