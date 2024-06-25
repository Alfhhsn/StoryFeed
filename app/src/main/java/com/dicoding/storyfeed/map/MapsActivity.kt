//package com.dicoding.storyfeed.map
//
//import android.os.Bundle
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import com.dicoding.storyfeed.R
//import com.dicoding.storyfeed.databinding.ActivityMapsBinding
//import com.dicoding.storyfeed.di.ResultState
//import com.dicoding.storyfeed.response.ListStoryItem
//import com.dicoding.storyfeed.view.ViewModelFactory
//import com.dicoding.storyfeed.view.base.BaseActivity
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//
//class MapsActivity : BaseActivity<ActivityMapsBinding>(), OnMapReadyCallback {
//
//    private lateinit var mMap: GoogleMap
//    private lateinit var binding: ActivityMapsBinding
//
//    private val viewModel: MapViewModel by viewModels {
//        ViewModelFactory.getInstance(this)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityMapsBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//    }
//
//    override fun getViewBinding(): ActivityMapsBinding {
//
//    }
//
//    override fun initUI() {
//
//    }
//
//    override fun initProcess() {
//
//    }
//
//    override fun initObservers() {
//        viewModel.fetchStories().observe(this) { result ->
//            when (result) {
//                is ResultState.Loading -> {
//                }
//
//                is ResultState.Success -> {
//                    val stories = result.data.listStory
//                    stories?.forEach { story ->
//                        story?.let { addMarker(it) }
//                    }
//
//                    if (stories != null) {
//                        if (stories.isNotEmpty()) {
//                            val firstStory = stories.first()
//                            val latLng = LatLng(
//                                (firstStory?.lat ?: 0.0) as Double,
//                                (firstStory?.lon ?: 0.0) as Double
//                            )
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
//                        }
//                    }
//                }
//
//                is ResultState.Error -> {
//                }
//            }
//        }
//    }
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//    }
//
//    private fun addMarker(data: ListStoryItem) {
//        val latLng = LatLng((data.lat ?: 0.0) as Double, (data.lon ?: 0.0) as Double)
//        mMap.addMarker(
//            MarkerOptions()
//                .position(latLng)
//                .title(data.name)
//                .snippet(data.description)
//        )
//    }
//}