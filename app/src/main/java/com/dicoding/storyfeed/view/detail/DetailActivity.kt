package com.dicoding.storyfeed.view.detail

import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.dicoding.storyfeed.databinding.ActivityDetailBinding
import com.dicoding.storyfeed.view.ViewModelFactory
import com.dicoding.storyfeed.view.base.BaseActivity

class DetailActivity : BaseActivity<ActivityDetailBinding>() {
    private val viewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    override fun getViewBinding(): ActivityDetailBinding {
        return ActivityDetailBinding.inflate(layoutInflater)
    }

    override fun initUI() {

    }

    override fun initProcess() {
        val id = intent.getStringExtra(EXTRA_ID)
        id?.let { viewModel.fetchStories(it) }
    }

    override fun initObservers() {
        viewModel.detailStories.observe(this) { stories ->
            binding.tvTitle.text = stories.name
            Glide.with(this)
                .load(stories.photoUrl)
                .into(binding.imgStory)
            binding.tvDescription.text = stories.description
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}