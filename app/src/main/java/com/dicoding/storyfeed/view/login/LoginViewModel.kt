package com.dicoding.storyfeed.view.login

import androidx.lifecycle.ViewModel
import com.dicoding.storyfeed.repo.UserRepository

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    private val _isError = MutableLiveData<String>()
//    val isError: LiveData<String> get() = _isError
//
//    private val _loginResponse = MutableLiveData<ResultState<LoginResponse>>()
//    val loginResponse: LiveData<ResultState<LoginResponse>> = _loginResponse

    fun loginUser(email: String, password: String)=
        userRepository.login(email, password)
}