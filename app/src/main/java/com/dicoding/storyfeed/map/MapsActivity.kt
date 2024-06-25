package com.dicoding.storyfeed.map

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
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
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : BaseActivity<ActivityMapsBinding>(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val boundsBuilder = LatLngBounds.Builder()

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
        // Add any process initialization here
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
                        story?.let { addMarker(it) }
                    }

                    if (stories.isNotEmpty()) {
                        val firstStory = stories.first()
                        val latLng = LatLng(
                            firstStory?.lat ?: 0.0,
                            firstStory?.lon ?: 0.0
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

        val dicodingSpace = LatLng(-6.8957643, 107.6338462)
        mMap.addMarker(
            MarkerOptions()
                .position(dicodingSpace)
                .title("Dicoding Space")
                .snippet("Batik Kumeli No.50")
        )
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dicodingSpace, 15f))
        getMyLocation()
        addManyMarker()
    }
    data class TourismPlace(
        val name: String,
        val latitude: Double,
        val longitude: Double
    )

    private fun addManyMarker() {
        val tourismPlace = listOf(
            TourismPlace("Floating Market Lembang", -6.8168954,107.6151046),
            TourismPlace("The Great Asia Africa", -6.8331128,107.6048483),
            TourismPlace("Rabbit Town", -6.8668408,107.608081),
            TourismPlace("Alun-Alun Kota Bandung", -6.9218518,107.6025294),
            TourismPlace("Orchid Forest Cikole", -6.780725, 107.637409),
        )
        tourismPlace.forEach { tourism ->
            val latLng = LatLng(tourism.latitude, tourism.longitude)
            mMap.addMarker(MarkerOptions().position(latLng).title(tourism.name))
            boundsBuilder.include(latLng)
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun addMarker(data: ListStoryItem) {
        val latLng = LatLng(data.lat ?: 0.0, data.lon ?: 0.0)
        mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("New Marker")
                .snippet("Lat: ${latLng.latitude} Long: ${latLng.longitude}")
                .icon(vectorToBitmap(R.drawable.ic_android, Color.parseColor("#3DDC84")))
        )
        mMap.setOnPoiClickListener { pointOfInterest ->
            val poiMarker = mMap.addMarker(
                MarkerOptions()
                    .position(pointOfInterest.latLng)
                    .title(pointOfInterest.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
            )
            poiMarker?.showInfoWindow()
        }
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
