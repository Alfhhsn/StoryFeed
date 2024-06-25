package com.dicoding.storyfeed.view.main

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyfeed.adapter.ListStoryAdapter
import com.dicoding.storyfeed.databinding.ActivityMainBinding
import com.dicoding.storyfeed.preferences.UserPreferences
import com.dicoding.storyfeed.view.ViewModelFactory
import com.dicoding.storyfeed.view.add.AddStoryActivity
import com.dicoding.storyfeed.view.base.BaseActivity
import com.dicoding.storyfeed.view.detail.DetailActivity
import com.dicoding.storyfeed.view.login.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private val adapter = ListStoryAdapter(arrayListOf()) { story ->
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_ID,story.id)
        startActivity(intent)
    }
    private lateinit var userPreferences: UserPreferences
    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initUI() {
        binding.listStory.layoutManager = LinearLayoutManager(this)
        binding.listStory.adapter = adapter

        binding.addStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

//        binding.mapButton.setOnClickListener {
//            val intent = Intent(this, MapsActivity::class.java)
//            startActivity(intent)
//        }
        binding.logoutButton.setOnClickListener {
            logout()
        }

        userPreferences = UserPreferences.getInstance(this)
    }

    override fun initProcess() {
        viewModel.fetchStories()
    }

    override fun initObservers() {
        viewModel.stories.observe(this) { stories ->
            adapter.setStories(stories)
        }
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.fetchStories()
    }

    private fun logout() {
//        val sharedPreferences: SharedPreferences = getSharedPreferences("your_preference_name", MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.clear()
//        editor.apply()
        lifecycleScope.launch(Dispatchers.IO)  {
            userPreferences.clearToken()
        }

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}