package com.dicoding.storyfeed.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.storyfeed.R
import com.dicoding.storyfeed.databinding.ActivitySplashBinding
import com.dicoding.storyfeed.preferences.UserPreferences
import com.dicoding.storyfeed.view.login.LoginActivity
import com.dicoding.storyfeed.view.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySplashBinding
    private lateinit var logo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        logo = findViewById(R.id.logo)
        startAnimation()

        val userPreference = UserPreferences.getInstance(this)

        lifecycleScope.launch {
            delay(2000)
            val token = userPreference.tokenFlow.firstOrNull()
            if (token != null) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }
            finish()
        }
    }

    private fun startAnimation() {
        val fade = AnimationUtils.loadAnimation(this, R.anim.fade)
        logo.startAnimation(fade)
    }
}