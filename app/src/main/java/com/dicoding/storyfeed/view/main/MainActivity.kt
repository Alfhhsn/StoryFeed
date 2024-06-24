package com.dicoding.storyfeed.view.main

import android.content.Intent
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyfeed.adapter.ListStoryAdapter
import com.dicoding.storyfeed.databinding.ActivityMainBinding
import com.dicoding.storyfeed.view.ViewModelFactory
import com.dicoding.storyfeed.view.add.AddStoryActivity
import com.dicoding.storyfeed.view.base.BaseActivity
import com.dicoding.storyfeed.view.detail.DetailActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    val adapter = ListStoryAdapter(arrayListOf()) { story ->
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_ID,story.id)
        startActivity(intent)
    }

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
    }

    override fun initProcess() {
        viewModel.fetchStories()
    }

    override fun initObservers() {
        viewModel.stories.observe(this) { stories ->
            adapter.setStories(stories)
        }
    }
}