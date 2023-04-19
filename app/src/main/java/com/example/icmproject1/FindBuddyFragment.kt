package com.example.icmproject1

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.icmproject1.adapter.ArtistAdapter
import com.example.icmproject1.data.Datasource
import com.example.icmproject1.model.Coordinates
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    private var buddyName = "";
    private var buddyLocation = Coordinates(0.0, 0.0)
    private var buddyMarker : Marker? = null
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var festivalUID : String = "Mso42n0MeMOZmKrKLfgQ"

    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            // Permission was granted, perform your operations here
            // For example, start updating user location
            setupMyLocation(googleMap)
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
                setupMyLocation(googleMap)
                useMyLocation { myLocation ->

                    val user = auth.currentUser
                    if (user != null) {
                        val uid = user.uid
                        Log.e("TAG", "uid: $uid")
                        firestore.collection("users").document(uid)
                            .update("coords", GeoPoint(myLocation.latitude, myLocation.longitude))
                            .addOnSuccessListener { documentReference ->
                                Log.d("TAG", "DocumentSnapshot added with ID: $documentReference")
                            }
                            .addOnFailureListener { e ->
                                Log.w("TAG", "Error adding document", e)
                            }
                    }

                }
            }

            // get user's festival festivalUID from firestore
            val user = auth.currentUser
            if (user != null) {
                // get user chosenFestival
                val docRef = firestore.collection("users").document(user.uid).get().addOnSuccessListener {
                    val festivalName = it.get("chosenFestival")
                    // get festivalUID from festivalName
                    val festivalRef = firestore.collection("festivals").whereEqualTo("name", festivalName).get().addOnSuccessListener {
                        festivalUID = it.documents[0].id
                        Log.e(ContentValues.TAG, "festivalUID: $festivalUID")

                        // Load data for all stages
                        val coroutineScope = CoroutineScope(Dispatchers.Main)

                        coroutineScope.launch {
                            val festival = Datasource(requireContext()).loadFestival(festivalUID)
                            val festivalLocation = LatLng(festival.coordinates.latitude, festival.coordinates.longitude)
                            val icon = generateMarkerIcon(R.drawable.festival)
                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(festivalLocation)
                                    .title(festival.name)
                                    .icon(icon)
                            )
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(festivalLocation, 10F))

                        }

                        view.findViewById<View>(R.id.festival_location_legend).setOnClickListener {
                            val coroutineScopeClick = CoroutineScope(Dispatchers.Main)
                            coroutineScopeClick.launch {
                                val selectedFestival = Datasource(requireContext()).loadFestival(festivalUID)
                                val festival = LatLng(selectedFestival.coordinates.latitude, selectedFestival.coordinates.longitude)
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(festival, 10F))
                            }
                        }

                    }
                }
            }

        })

        // add click listeners to map legend entries
        view.findViewById<View>(R.id.my_location_legend).setOnClickListener {
            useMyLocation { myLocation ->
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(myLocation.latitude, myLocation.longitude), 10F))
            }
        }
        view.findViewById<View>(R.id.buddy_location_legend).setOnClickListener {
            if (buddyLocation.latitude != 0.0 && buddyLocation.longitude != 0.0) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(buddyLocation.latitude, buddyLocation.longitude), 10F))
            } else {
                Toast.makeText(context, "No Buddy Selected", Toast.LENGTH_SHORT).show()
            }
        }

        // handle buddy search
        val inputFindBuddy = view.findViewById<EditText>(R.id.input_find_buddy)
        val findButton = view.findViewById<Button>(R.id.button_find)
        findButton.setOnClickListener {
            buddyName = inputFindBuddy.text.toString()
            var buddyUID = ""
            val docRef = firestore.collection("users").whereEqualTo("username", buddyName).get().addOnSuccessListener {
                // check if none found
                if (it.documents.isEmpty()) {
                    Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }
                buddyUID = it.documents[0].id
                Log.e(ContentValues.TAG, "buddyUID: $buddyUID")
                val coroutineScope = CoroutineScope(Dispatchers.Main)
                coroutineScope.launch {
                    buddyLocation = Datasource(requireContext()).getUserLocation(buddyUID)
                    Log.d(ContentValues.TAG, "buddyLocation: ${buddyLocation.latitude}, ${buddyLocation.longitude}")
                    if (buddyLocation.latitude == 0.0 && buddyLocation.longitude == 0.0) {
                        Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
                    } else {
                        // remove old marker
                        buddyMarker?.remove()

                        val icon = generateMarkerIcon(R.drawable.friend_location)
                        buddyMarker = googleMap.addMarker(
                            MarkerOptions()
                                .position(LatLng(buddyLocation.latitude, buddyLocation.longitude))
                                .title(buddyName)
                                .icon(icon)
                        )
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(buddyLocation.latitude, buddyLocation.longitude), 10F))
                    }

                }


            }
        }

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

    private fun generateMarkerIcon(iconId : Int) : BitmapDescriptor {
        val originalBitmap = BitmapFactory.decodeResource(resources, iconId)
        val density = resources.displayMetrics.density
        val dpWidth = 40f
        val dpHeight = 40f
        val scaledWidth = (dpWidth * density).toInt()
        val scaledHeight = (dpHeight * density).toInt()
        val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, scaledWidth, scaledHeight, false)
        return BitmapDescriptorFactory.fromBitmap(scaledBitmap)
    }

    @SuppressLint("MissingPermission")
    private fun useMyLocation(callback: (Location) -> Unit) {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null)
                callback(location)
        }
    }

    @SuppressLint("MissingPermission")
    private fun setupMyLocation(googleMap: GoogleMap) {
        googleMap.isMyLocationEnabled = true
        useMyLocation { location ->
            val latLng = LatLng(location.latitude, location.longitude)

            // Add a marker at the user's location
            val icon = generateMarkerIcon(R.drawable.my_location)
            googleMap.addMarker(MarkerOptions().position(latLng).icon(icon))
        }
    }
}
