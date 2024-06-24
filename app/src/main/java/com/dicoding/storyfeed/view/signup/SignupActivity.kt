package com.dicoding.storyfeed.view.signup

import android.content.Intent
import androidx.activity.viewModels
import com.dicoding.storyfeed.databinding.ActivitySignupBinding
import com.dicoding.storyfeed.di.ResultState
import com.dicoding.storyfeed.util.showError
import com.dicoding.storyfeed.view.ViewModelFactory
import com.dicoding.storyfeed.view.base.BaseActivity
import com.dicoding.storyfeed.view.login.LoginActivity

class SignupActivity : BaseActivity<ActivitySignupBinding>() {
    private val signupViewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun getViewBinding(): ActivitySignupBinding {
        return ActivitySignupBinding.inflate(layoutInflater)
    }

    override fun initUI() {
        binding.signupButton.setOnClickListener {
            register()
        }
    }

    override fun initProcess() {

    }

    override fun initObservers() {

    }

    private fun register(){
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        when {
            name.isEmpty() -> {
                binding.nameEditText.showError("nama masih kosong")
            }
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
                signupViewModel.signupUser(name, email, password).observe(this){response ->
                    when (response){
                        is ResultState.Error -> {
                            showToast("error")
                        }
                        ResultState.Loading -> {
                            showLoadingDialog()
                        }
                        is ResultState.Success -> {
                            showToast("Success Register")
                            closeLoadingDialog()

                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}