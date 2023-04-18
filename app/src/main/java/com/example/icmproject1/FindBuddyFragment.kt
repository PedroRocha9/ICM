package com.example.icmproject1

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.icmproject1.data.Datasource
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FindBuddyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FindBuddyFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var mapFragment : SupportMapFragment
    lateinit var googleMap: GoogleMap

    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            // Permission was granted, perform your operations here
            // For example, start updating user location
            googleMap.isMyLocationEnabled = true
        } else {
            // Permission denied, handle the user's response here
            // For example, show a message explaining why the permission is necessary
            Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_find_buddy, container, false)

        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )

            } else {
                googleMap.isMyLocationEnabled = true
                val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val latLng = LatLng(location.latitude, location.longitude)

                        // Add a marker at the user's location
                        val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.my_location)
                        val density = resources.displayMetrics.density
                        val dpWidth = 40f
                        val dpHeight = 40f
                        val scaledWidth = (dpWidth * density).toInt()
                        val scaledHeight = (dpHeight * density).toInt()
                        val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, scaledWidth, scaledHeight, false)
                        val icon = BitmapDescriptorFactory.fromBitmap(scaledBitmap)
                        googleMap.addMarker(MarkerOptions().position(latLng).icon(icon))
                    }
                }
            }

            val selectedFestival = Datasource(requireContext()).loadFestivalEntries()[0]
            val festival = LatLng(selectedFestival.coordinates.latitude, selectedFestival.coordinates.longitude)

            val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.festival)
            val density = resources.displayMetrics.density
            val dpWidth = 40f
            val dpHeight = 40f
            val scaledWidth = (dpWidth * density).toInt()
            val scaledHeight = (dpHeight * density).toInt()
            val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, scaledWidth, scaledHeight, false)
            val icon = BitmapDescriptorFactory.fromBitmap(scaledBitmap)

            googleMap.addMarker(
                MarkerOptions()
                    .position(festival)
                    .title(selectedFestival.name)
                    .icon(icon)
            )
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(festival, 10F))
        })

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FindBuddyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FindBuddyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
