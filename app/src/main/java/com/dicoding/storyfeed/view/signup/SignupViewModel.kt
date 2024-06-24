package com.dicoding.storyfeed.view.signup

import androidx.lifecycle.ViewModel
import com.dicoding.storyfeed.repo.UserRepository

class SignupViewModel (private val userRepository: UserRepository) : ViewModel(){
    fun signupUser(name: String, email: String, password: String)=
        userRepository.signup(name, email, password)
}