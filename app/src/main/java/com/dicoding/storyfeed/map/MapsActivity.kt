package com.dicoding.storyfeed.map

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.dicoding.storyfeed.R
import com.dicoding.storyfeed.databinding.ActivityMapsBinding
import com.dicoding.storyfeed.di.ResultState
import com.dicoding.storyfeed.response.ListStoryItem
import com.dicoding.storyfeed.view.ViewModelFactory
import com.dicoding.storyfeed.view.base.BaseActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : BaseActivity<ActivityMapsBinding>(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private val viewModel: MapViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    override fun getViewBinding(): ActivityMapsBinding {
        return ActivityMapsBinding.inflate(layoutInflater)
    }

    override fun initUI() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun initProcess() {

    }

    override fun initObservers() {
        viewModel.stories.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    showLoadingDialog()
                }
                is ResultState.Success -> {
                    closeLoadingDialog()
                    val stories = result.data
                    stories.forEach { story ->
                        addMarker(story)
                    }

                    if (stories.isNotEmpty()) {
                        val firstStory = stories.first()
                        val latLng = LatLng(
                            firstStory.lat ?: 0.0,
                            firstStory.lon ?: 0.0
                        )
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                    }
                }
                is ResultState.Error -> {
                    closeLoadingDialog()
                    showToast(result.error)
                }
            }
        }

        viewModel.fetchStories()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
    }
    private fun addMarker(data: ListStoryItem) {
        val latLng = LatLng(data.lat ?: 0.0, data.lon ?: 0.0)
        mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(data.name)
                .snippet(data.description)
                .icon(vectorToBitmap(R.drawable.ic_location, Color.parseColor("#3DDC84")))
        )
    }

    private fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
        if (vectorDrawable == null) {
            Log.e("BitmapHelper", "Resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        }
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}
