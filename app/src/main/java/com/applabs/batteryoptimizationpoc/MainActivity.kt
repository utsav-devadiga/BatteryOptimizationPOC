package com.applabs.batteryoptimizationpoc

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.PowerManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager
    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    private var wakeLock: PowerManager.WakeLock? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        // Acquire a wake lock to keep the CPU running even when the screen is off
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag")
        wakeLock?.acquire()

        // Check if permissions are granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the necessary permissions
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permissions are already granted
            startLocationUpdates(this)
        }
    }

    private fun startLocationUpdates(activity: Activity) {
        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                100, // Reduce the interval to 100ms for frequent updates
                0f, // 0 meters: Get updates even if the user doesn't move
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        location.let {
                            // Handle location update
                            Toast.makeText(activity, "Location Changed", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                        // Handle provider status changes
                        Toast.makeText(activity, status.toString(), Toast.LENGTH_SHORT).show()
                    }

                    override fun onProviderEnabled(provider: String) {
                        // Handle provider being enabled
                        Toast.makeText(activity, "Enabled Provider", Toast.LENGTH_SHORT).show()
                    }

                    override fun onProviderDisabled(provider: String) {
                        // Handle provider being disabled
                        Toast.makeText(activity, "Disabled Provider", Toast.LENGTH_SHORT).show()
                    }
                })
        } catch (e: SecurityException) {
            // Handle case where the user denies the permission request
            e.printStackTrace()
        }
    }

    // Handle the result of the permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted, start location updates
                startLocationUpdates(this)
            } else {
                // Permission denied, show a message to the user
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release the wake lock when the activity is destroyed to save battery
        wakeLock?.release()
    }
}
