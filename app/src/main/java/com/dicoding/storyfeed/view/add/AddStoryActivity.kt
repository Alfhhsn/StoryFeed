package com.dicoding.storyfeed.view.add

import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.dicoding.storyfeed.R
import com.dicoding.storyfeed.databinding.ActivityAddStoryBinding
import com.dicoding.storyfeed.util.getImageUri
import com.dicoding.storyfeed.util.reduceFileImage
import com.dicoding.storyfeed.util.uriToFile
import com.dicoding.storyfeed.view.ViewModelFactory
import com.dicoding.storyfeed.view.base.BaseActivity

class AddStoryActivity : BaseActivity<ActivityAddStoryBinding>() {
    private val viewModel: AddViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private var currentImageUri: Uri? = null

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    override fun getViewBinding(): ActivityAddStoryBinding {
        return ActivityAddStoryBinding.inflate(layoutInflater)
    }

    override fun initUI() {
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener { uploadStory() }
    }

    override fun initProcess() {

    }

    override fun initObservers() {
        viewModel.storiesResponse.observe(this) { result ->
            showLoading(false)
            if (!result.error) {
                finish()
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.ivPhotoPreview.setImageURI(currentImageUri)
        }
    }

    private fun uploadStory() {
        val description = binding.addDescription.text.toString()
        val imageFile = currentImageUri?.let { uriToFile(it, this).reduceFileImage() }
        if (imageFile != null) {
            viewModel.uploadStory(imageFile, description)
            showLoading(true)
        } else {
            showToast(getString(R.string.empty_image_warning))
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}