package com.example.opsc7312part1

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment


class GoogleMapsFragment : Fragment(), OnMapReadyCallback {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_google_maps, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!checkLocationPermission() || !isGpsProviderEnabled() || !areLocationServicesEnabled() )
        {
            showLocationPermissionDialog()
        }

        if(!isGooglePlayServicesAvailable())
        {
            showGooglePlayServicesErrorDialog()
        }


        if(checkLocationPermission() && isGpsProviderEnabled() && areLocationServicesEnabled() && isGooglePlayServicesAvailable())
        {
            val mapFragment = childFragmentManager
                .findFragmentById(R.id.mapView) as SupportMapFragment
            mapFragment.getMapAsync(this)

        }

    }

    private fun checkLocationPermission() : Boolean{
      return ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isGpsProviderEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun areLocationServicesEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun isGooglePlayServicesAvailable(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(requireContext())
        return resultCode == ConnectionResult.SUCCESS
    }



    private fun RedirectToSettings(){
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        startActivity(intent)
    }
    private fun showLocationPermissionDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Location Permission")
        builder.setMessage("To use this feature, you need to enable location permission. Do you want to go to settings now?")

        builder.setPositiveButton("Yes") { dialog, which ->
            // User clicked "Yes"
            dialog.dismiss()
            RedirectToSettings()
        }

        builder.setNegativeButton("No") { dialog, which ->
            // User clicked "No"
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun showGooglePlayServicesErrorDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Google Play Services Error")
        builder.setMessage("Google Play services are not available or not working properly.")

        builder.setPositiveButton("OK") { dialog, which ->
            // User clicked "OK"
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    override fun onMapReady(p0: GoogleMap) {
        TODO("Not yet implemented")
    }


}

