package com.dicoding.storyfeed.view.base

import android.Manifest
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: VB? = null
    open val binding get() = _binding!!
    private lateinit var loadingDialog : ProgressDialog

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
        setContentView(_binding?.root)
        loadingDialog = ProgressDialog(this)

        initIntent()
        initUI()
        initAdapter()
        initActions()
        initProcess()
        initObservers()
    }

    abstract fun getViewBinding(): VB

    abstract fun initUI()

    abstract fun initProcess()

    abstract fun initObservers()

    protected open fun initIntent() {}

    protected open fun initAdapter() {}

    protected open fun initActions() {}

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    internal fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    open fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    open fun showLoadingDialog() {
        loadingDialog.setTitle("Please wait")
        loadingDialog.setMessage("Loading")
        loadingDialog.show()
    }

    open fun closeLoadingDialog() {
        loadingDialog.hide()
    }

    companion object {
        internal const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
