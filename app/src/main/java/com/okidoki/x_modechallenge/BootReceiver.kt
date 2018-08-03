package com.okidoki.x_modechallenge

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import android.widget.Toast

class BootReceiver: BroadcastReceiver() {

    private val TAG = BootReceiver::class.java.simpleName.toString()

    val timeElapse = if (BuildConfig.DEBUG) 60000 else AlarmManager.INTERVAL_HOUR

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            setRepeatingLocationAlarm(context)
            Log.d(TAG, "DEVICE REBOOTED");
        }
    }

    /**
     * restarts the alarm after the device reboots, ensures that the alarm automatically continues after the device is restarted
     */
    private fun setRepeatingLocationAlarm(context: Context) {
        val intent = Intent(context, LocationReceiver::class.java)
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val alarmIntent = PendingIntent.getBroadcast(context, LOCATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), timeElapse, alarmIntent)

        Toast.makeText(context, R.string.alarm_set, Toast.LENGTH_SHORT).show()
        Log.d(TAG, "alarm was reset, after reboot");
    }
}