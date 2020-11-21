package com.mapboxcustomviewnavigation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mapbox.android.core.permissions.PermissionsListener
import java.util.ArrayList

class MainActivity : AppCompatActivity(), PermissionsListener {
    private lateinit var mActivity: Activity
    private val permissionsHelper = LocationPermissionsHelper(this)
    private lateinit var txtGenerateRoute: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mActivity = this@MainActivity

        txtGenerateRoute = findViewById(R.id.txtGenerateRoute)

        when (LocationPermissionsHelper.areLocationPermissionsGranted(this)) {
            true -> requestPermissionIfNotGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            else -> permissionsHelper.requestLocationPermissions(this)
        }

        txtGenerateRoute.setOnClickListener {
            var mIntent: Intent = Intent(mActivity, GenerateRouteActivity::class.java)
            startActivity(mIntent)
        }
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
        Toast
            .makeText(
                this,
                "This app needs location and storage permissions" +
                        "in order to show its functionality.",
                Toast.LENGTH_LONG
            ).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            requestPermissionIfNotGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            Toast.makeText(this, "You didn't grant location permissions.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSIONS_REQUEST_CODE) {
            permissionsHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
        } else {
            when (
                grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                true -> {
//                    txtSampleRealtimeNavigation.isClickable = true
                }
                else -> {
//                    txtSampleRealtimeNavigation.isClickable = false
                    Toast.makeText(
                        this,
                        "You didn't grant storage or location permissions.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun requestPermissionIfNotGranted(permission: String) {
        val permissionsNeeded = ArrayList<String>()
        if (ContextCompat
                .checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsNeeded.add(permission)
            ActivityCompat.requestPermissions(this, permissionsNeeded.toTypedArray(), 10)
        }
    }
}
