package com.okidoki.x_modechallenge

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
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

}