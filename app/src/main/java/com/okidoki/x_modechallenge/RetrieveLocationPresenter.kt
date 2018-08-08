package com.okidoki.x_modechallenge

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.SystemClock
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity

class RetrieveLocationPresenter(private val mView: RetrieveLocationActivity) : RetrieveLocationContract.UserActionsListener {

    val timeElapse = if (BuildConfig.DEBUG) 60000 else AlarmManager.INTERVAL_HOUR

    /**
     * setRepeatingLocationAlarm(Context, Long)
     * Registers the AlarmManager to fire the LocationReceiver to perform the location retrieval
     */
    override fun setRepeatingLocationAlarm(context: Context) {
        val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, LocationReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(context, LOCATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),
        timeElapse, alarmIntent)

        mView.showRepeatingEventSetToast()
    }

    /**
     * Check if the location permission has been granted and setup the AlarmManager.
     * If permission is not granted determine if rationale should be shown to the user, then sends a
     * permission request. The first request will not show the rationale
     */
    override fun startLocationAlarm(activity: RetrieveLocationActivity) {
        // Runtime permission required for Android 5.0 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)

            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                setRepeatingLocationAlarm(activity)
            } else {
                // Permission not granted
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    mView.showLocationRationale()
                } else {
                    mView.sendLocationPermissionRequest()
                }
            }
        else {
            setRepeatingLocationAlarm(activity)
        }
    }
}