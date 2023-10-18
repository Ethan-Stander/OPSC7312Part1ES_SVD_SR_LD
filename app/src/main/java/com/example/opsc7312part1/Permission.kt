package com.example.opsc7312part1
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import android.Manifest
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

private lateinit var btnGrant: Button
class Permission : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if ((ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            (requireActivity() as FragmentTesting).replaceFragment(map_nearby(), "map")
            //activity?.finish()
            return
        }

        btnGrant = view.findViewById<Button>(R.id.btn_grant)
        btnGrant.setOnClickListener {
            Dexter.withActivity(requireActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        (requireActivity() as FragmentTesting).replaceFragment(map_nearby(), "map")

                        //activity?.finish()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        if (response.isPermanentlyDenied) {
                            val builder = AlertDialog.Builder(requireActivity())
                            builder.setTitle("Permission Denied")
                                .setMessage("Permission to access device location is permanently denied. You need to go to settings to allow the permission.")
                                .setNegativeButton("Cancel", null)
                                .setPositiveButton("OK") { dialog, which ->
                                    val intent = Intent()
                                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                    intent.data = Uri.fromParts("package", activity?.packageName, null)
                                    startActivity(intent)
                                }
                                .show()
                        } else {
                            Toast.makeText(requireActivity(), "Permission Denied", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                })
                .check()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_permission, container, false)
    }
}



