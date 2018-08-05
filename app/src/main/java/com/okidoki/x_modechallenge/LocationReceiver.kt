package com.okidoki.x_modechallenge

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

private const val TIME_OUT_TIMER: Long =  20000

class LocationReceiver: BroadcastReceiver() {

    private val TAG = LocationReceiver::class.java.simpleName.toString()

    private lateinit var mLocationManager: LocationManager

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private var mLocation: Location? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "Received: ${SystemClock.currentThreadTimeMillis()}")
        mLocationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val locationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                mLocation = location
            }
            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}
            override fun onProviderEnabled(p0: String?) {}
            override fun onProviderDisabled(p0: String?) {}
        }

        getLocation(context,locationListener)

        val handler = Handler()
        val runnableCode = Runnable {
            if (mLocation == null) {
                mLocationManager.removeUpdates(locationListener)
                getLastKnownLocation(context)
            } else {
                displayLocationToast(context, mLocation, "Network Provider")
            }
            Log.d(TAG, "Handler called")
        }
        handler.postDelayed(runnableCode, TIME_OUT_TIMER)
    }

    /**
     * Checks the users location permission and then requests for the users current location using the NETWORK PROVIDER
     */
    private fun getLocation(context: Context, locationListener: LocationListener){
        // check for location permission
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // I'm using the NETWORK_PROVIDER because it determines user location using cell tower and Wi-Fi signals and it works
            // indoors and outdoors, responds faster, and uses less battery power

            // Register the listener with the Location Manager
            mLocationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null)
        } else {
            Toast.makeText(context, R.string.location_permission_required, Toast.LENGTH_LONG).show()
            // TODO: cancel alarm here
        }

    }

    /**
     * Checks the users location permission and then requests for the users last known location using the FUSED LOCATION PROVIDER
     */
    private fun getLastKnownLocation(context: Context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mFusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        displayLocationToast(context, location, "Fused Provider")
                    }
        } else {
            Toast.makeText(context, R.string.location_permission_required, Toast.LENGTH_LONG).show()
            // TODO: cancel alarm here
        }
    }

    /**
     * Displays the users location data with a toast
     */
    private fun displayLocationToast(context: Context, location: Location?, str: String) {
        var msg: String
        if (location == null) {
            msg = str + "ERROR: No location available"
            Log.d(TAG, "str: $msg")

        } else {
            msg = "LON: ${location.longitude} LAT: ${location.latitude}"
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            Log.d(TAG, "str: $msg")

            // TODO: persist location data
            // 1. shared preferences - key-value pair storage of the device, for small amount of data, typically used for setting preferences
            // 2. Local database - using a SQLite database or using Room, which provides an interface on top of SQLite making it easier to manage
            // 3. File storage - there is an internal and external file storage option
        }
    }
}