package com.example.opsc7312part1

import android.annotation.SuppressLint
import android.content.IntentSender
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.mancj.materialsearchbar.MaterialSearchBar
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Arrays
import kotlin.math.roundToInt

val nearbyStoresList: MutableList<Store> = mutableListOf()
private const val DEFAULT_ZOOM = 15f
class map_nearby() : Fragment(), OnMapReadyCallback, NearbySearchTask.NearbySearchCallback {
    private lateinit var appBarConfiguration: AppBarConfiguration

    private var mMap: GoogleMap?=null
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var placesClient: PlacesClient
    private lateinit var predictionList: List<AutocompletePrediction>

    private var mLastKnownLocation: Location? = null
    private lateinit  var locationCallback: LocationCallback

    private lateinit var materialSearchBar: MaterialSearchBar
    private  var mapView: View? = null
    private lateinit var btnFind: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nearbyStoresList.clear()
        materialSearchBar = view.findViewById(R.id.searchBar)
        btnFind = view.findViewById(R.id.btn_find)
        btnFind.isEnabled = false
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapView = mapFragment.view
        mapFragment.getMapAsync(this)


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        Places.initialize(requireActivity(), getString(R.string.google_maps_api))
        placesClient = Places.createClient(requireActivity())

        val token = AutocompleteSessionToken.newInstance()

        materialSearchBar!!.setOnSearchActionListener(object :
            MaterialSearchBar.OnSearchActionListener {
            override fun onSearchStateChanged(enabled: Boolean) {}
            override fun onSearchConfirmed(text: CharSequence) {
                requireActivity().startSearch(text.toString(), true, null, true)
            }

            override fun onButtonClicked(buttonCode: Int) {
                if (buttonCode == MaterialSearchBar.BUTTON_NAVIGATION) {

                } else if (buttonCode == MaterialSearchBar.BUTTON_BACK) {
                    materialSearchBar.disableSearch()
                }
            }
        })


        materialSearchBar!!.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val predictionsRequest = FindAutocompletePredictionsRequest.builder()
                    .setTypeFilter(TypeFilter.ADDRESS)
                    .setSessionToken(token)
                    .setQuery(s.toString())
                    .build()
                placesClient.findAutocompletePredictions(predictionsRequest)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val predictionsResponse = task.result
                            if (predictionsResponse != null) {
                                predictionList = predictionsResponse.autocompletePredictions
                                val suggestionsList: MutableList<String?> =
                                    ArrayList()
                                for (i in predictionList.indices) {
                                    val prediction = predictionList.get(i)
                                    suggestionsList.add(prediction.getFullText(null).toString())
                                }
                                materialSearchBar!!.updateLastSuggestions(suggestionsList)
                                if (!materialSearchBar!!.isSuggestionsVisible) {
                                    materialSearchBar!!.showSuggestionsList()
                                }
                            }
                        } else {
                            Log.i("mytag", "prediction fetching task unsuccessful")
                        }
                    }
            }
            override fun afterTextChanged(s: Editable) {}
        })

        materialSearchBar.setSuggstionsClickListener(object :
            SuggestionsAdapter.OnItemViewClickListener {
            override fun OnItemClickListener(position: Int, v: View?) {
                if (position >= predictionList!!.size) {
                    return
                }
                val selectedPrediction = predictionList!![position]
                val suggestion = materialSearchBar!!.lastSuggestions[position].toString()
                materialSearchBar!!.text = suggestion
                Handler().postDelayed(Runnable { materialSearchBar!!.clearSuggestions() }, 1000)
                val imm = requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.hideSoftInputFromWindow(
                    materialSearchBar!!.windowToken,
                    InputMethodManager.HIDE_IMPLICIT_ONLY
                )
                val placeId = selectedPrediction.placeId
                val placeFields = Arrays.asList(Place.Field.LAT_LNG)
                val fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build()
                placesClient.fetchPlace(fetchPlaceRequest)
                    .addOnSuccessListener { fetchPlaceResponse ->
                        val place = fetchPlaceResponse.place
                        Log.i("mytag", "Place found: " + place.name)
                        val latLngOfPlace = place.latLng
                        if (latLngOfPlace != null) {
                            mMap!!.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    latLngOfPlace,
                                    DEFAULT_ZOOM
                                )
                            )
                        }
                    }.addOnFailureListener { e ->
                        if (e is ApiException) {
                            val apiException = e
                            apiException.printStackTrace()
                            val statusCode = apiException.statusCode
                            Log.i("mytag", "place not found: " + e.message)
                            Log.i("mytag", "status code: $statusCode")
                        }
                    }
            }
            override fun OnItemDeleteListener(position: Int, v: View?) {}
        })
        btnFind.setOnClickListener(object : View.OnClickListener {  // btn find stores
            @SuppressLint("MissingPermission")
            override fun onClick(v: View?) {

                val mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

                // Define the Nearby Search parameters
                val apiKey = getString(R.string.google_maps_api)
                val type = "hardware_store"

                // Get the phone's current location
                mFusedLocationProviderClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            val latitude = location.latitude
                            val longitude = location.longitude
                            val locationString = "$latitude,$longitude"
                            // Execute the Nearby Search task with the current location
                            executeNearbySearch(apiKey, locationString, type)
                            (requireActivity() as FragmentTesting).replaceFragment(Nearby_Stores(), "Nearby Stores")

                        } else {
                            // Handle the case where location is null
                            Toast.makeText(
                                requireActivity(),
                                "Failed to get current location.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .addOnFailureListener { exception: Exception ->
                        // Handle any errors that occurred while getting location
                        Toast.makeText(
                            requireActivity(),
                            "Failed to get current location: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        })
    }
    private fun executeNearbySearch(apiKey: String, location: String, type: String) {
        val user = User(
            UserID = UserID,
            Username = UserName
        )
        var radius:Int =50000
        var url:String
        lifecycleScope.launch {
            val setting = FirebaseUtils.Get(user)
            setting?.let {
                Log.i("ERROR","hi")
                if (!setting.KM) {
                    radius = (it.MaxDistance * 1609.34 ).roundToInt()
                } else {
                    radius = (it.MaxDistance * 1000).roundToInt()
                }
                Log.i("ERROR",radius.toString())

                url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=$apiKey&location=$location&radius="+radius.toString()+"&type=$type"
                val nearbySearchTask = NearbySearchTask(this@map_nearby,mMap)

                // Execute the NearbySearchTask
                nearbySearchTask.execute(url)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_nearby, container, false)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap) {
    mMap = p0
    mMap!!.isMyLocationEnabled = true
    mMap!!.uiSettings.isMyLocationButtonEnabled = true


    if ((mapView != null) && (mapView!!.findViewById<View>(R.id.map) != null)) {
        val locationButton: View =
            (mapView!!.findViewById<View>(R.id.map).parent as View).findViewById("2".toInt())
        val layoutParams = locationButton.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = 120
        layoutParams.height = 120
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        layoutParams.setMargins(0, 0, 20, 20)
    }

    //check if gps is enabled or not and then request user to enable it
    val locationRequest: LocationRequest = LocationRequest.create()
    locationRequest.setInterval(10000)
    locationRequest.setFastestInterval(5000)
    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    val settingsClient = LocationServices.getSettingsClient(requireActivity())
    val task = settingsClient.checkLocationSettings(builder.build())

    task.addOnSuccessListener(requireActivity(),
        OnSuccessListener<LocationSettingsResponse?> { getDeviceLocation() })
    task.addOnFailureListener(requireActivity(), OnFailureListener { e ->
        if (e is ResolvableApiException) {
            try {
                e.startResolutionForResult(requireActivity(), 51)
            } catch (e1: IntentSender.SendIntentException) {
                e1.printStackTrace()
            }
        }
    })
    mMap!!.setOnMyLocationButtonClickListener {
        if (materialSearchBar!!.isSuggestionsVisible) materialSearchBar!!.clearSuggestions()
        if (materialSearchBar.isSearchEnabled()) materialSearchBar.disableSearch()
        false
    }
}

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        mFusedLocationProviderClient!!.lastLocation
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mLastKnownLocation = task.result
                    if (mLastKnownLocation != null) {
                        mMap!!.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    mLastKnownLocation!!.latitude,
                                    mLastKnownLocation!!.longitude
                                ), DEFAULT_ZOOM
                            )
                        )
                        // Trigger nearby search after camera has moved to device's location
                        val apiKey = getString(R.string.google_maps_api)
                        val type = "hardware_store"

                        val locationString = "${mLastKnownLocation!!.latitude},${mLastKnownLocation!!.longitude}"
                        executeNearbySearch(apiKey, locationString, type)

                    } else {
                        val locationRequest: LocationRequest = LocationRequest.create()
                        locationRequest.setInterval(10000)
                        locationRequest.setFastestInterval(5000)
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        locationCallback = object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult) {
                                super.onLocationResult(locationResult)
                                if (locationResult == null) {
                                    return
                                }
                                mLastKnownLocation = locationResult.lastLocation!!
                                mMap!!.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(
                                            mLastKnownLocation!!.latitude,
                                            mLastKnownLocation!!.longitude
                                        ), DEFAULT_ZOOM
                                    )
                                )
                                mFusedLocationProviderClient!!.removeLocationUpdates(
                                    locationCallback!!
                                )
                            }
                        }
                        mFusedLocationProviderClient!!.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            null
                        )
                    }
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "unable to get last location",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
    }

    override fun onNearbySearchComplete() {
        btnFind.isEnabled = true
    }
}
class NearbySearchTask(private val callback: NearbySearchCallback,private val mMap: GoogleMap?): AsyncTask<String, Void, String>() {
    private val TAG = "NearbySearchTask"
    interface NearbySearchCallback {
        fun onNearbySearchComplete()
    }
    override fun doInBackground(vararg params: String?): String {
        val urlString = params[0]
        var result = ""

        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            var line: String?
            val response = StringBuilder()

            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()
            result = response.toString()
        } catch (e: Exception) {

        }
        return result
    }

    override fun onPostExecute(result: String?) {
        // Process the result, which is in JSON format
        if (!result.isNullOrBlank()) {
            processNearbySearchResult(result)
            callback.onNearbySearchComplete() // Notify that the task is complete

            val placedStores = mutableSetOf<LatLng>() // Set to keep track of placed markers

            val databaseReference = FirebaseDatabase.getInstance().getReference("Users/$UserID/MyStores")
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //place the markers
                    for (storeSnapshot in dataSnapshot.children) {
                        val store = storeSnapshot.getValue(Store::class.java)
                        if (store != null) {
                            val storeLatLng = LatLng(store.latitude, store.longitude)
                            mMap?.addMarker(
                                MarkerOptions()
                                    .position(storeLatLng)
                                    .title(store.name)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            )
                            placedStores.add(storeLatLng)
                        }
                    }

                    for (store in nearbyStoresList) {
                        val storeLatLng = LatLng(store.latitude, store.longitude)
                        if (!placedStores.contains(storeLatLng)) {
                            mMap?.addMarker(MarkerOptions().position(storeLatLng).title(store.name))
                        }

                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.i("Error","databaseError")
                }
            })
        }
    }

    private fun processNearbySearchResult(jsonData: String) {
        try {
            val jsonObject = JSONObject(jsonData)
            val resultsArray = jsonObject.getJSONArray("results")

            for (i in 0 until resultsArray.length()) {
                val placeObject = resultsArray.getJSONObject(i)
                val name = placeObject.getString("name")
                val vicinity = placeObject.getString("vicinity")
                val latitude = placeObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat")
                val longitude = placeObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng")
                val placeID = placeObject.getString("place_id")
                val ratings = placeObject.getInt("rating")
                val status = placeObject.getString("business_status")

                val isOpen = if (placeObject.has("opening_hours") && !placeObject.isNull("opening_hours")) {
                    placeObject.getJSONObject("opening_hours").getBoolean("open_now")
                } else {
                    false
                }

                // Create a Store object and add it to the nearbyStoresList
                val store = Store(name, vicinity, latitude, longitude, placeID, status, ratings,isOpen,false)
                nearbyStoresList.add(store)

            }
        } catch (e: Exception) {
            Log.i("Error",e.message.toString())
        }
    }
}
