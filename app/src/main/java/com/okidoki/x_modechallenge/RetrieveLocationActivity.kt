package com.okidoki.x_modechallenge

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast


const val LOCATION_REQUEST_CODE = 0
const val PERMISSION_REQUEST_LOCATION = 1

class RetrieveLocationActivity : AppCompatActivity(), RetrieveLocationContract.View {

    val TAG = RetrieveLocationActivity::class.java.simpleName.toString()

    private lateinit var textView: TextView

    private lateinit var mActionListener: RetrieveLocationContract.UserActionsListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mActionListener = RetrieveLocationPresenter(mView = this);

        // Make the "Hello" clickable in the textView
        textView = findViewById(R.id.id_text_view)
        val txt = SpannableString(textView.text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                Log.d(TAG, "Hello text clicked")
                startLocationAlarm()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }
        txt.setSpan(clickableSpan, 0, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        textView.text = txt
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.highlightColor = Color.TRANSPARENT
    }

    override fun showAccessGrantedToast() {
        Toast.makeText(this, R.string.location_permission_granted, Toast.LENGTH_SHORT).show()
    }

    override fun showAccessDeniedToast() {
        Toast.makeText(this, R.string.location_permission_denied, Toast.LENGTH_SHORT).show()
    }

    override fun showRepeatingEventSetToast() {
        Toast.makeText(this, R.string.alarm_set, Toast.LENGTH_SHORT).show()
    }

    override fun showLocationRationale() {
        Snackbar.make(findViewById(R.id.root_layout), R.string.location_permission_required,
                Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, {
            sendLocationPermissionRequest()
        }).show()
    }

    override fun sendLocationPermissionRequest(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_LOCATION)
    }

    /**
     * onRequestPermission()
     * Called after the user responds to a permission request, if request is cancelled, the result arrays are empty.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    showAccessGrantedToast()
                    mActionListener.setRepeatingLocationAlarm(this)
                } else {
                    showAccessDeniedToast()
                }
                return
            }
        }
    }

    /**
     * Check if the location permission has been granted, setup the AlarmManager if it is
     */
    private fun startLocationAlarm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mActionListener.setRepeatingLocationAlarm(this)
        } else {
            requestLocationPermission()
        }
    }

    /**
     * requestLocationPermission()
     * Determines if rationale should be shown to the user, then sends a permission request. The first
     * request will not show the rationale
     */
    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            showLocationRationale()
        } else {
            sendLocationPermissionRequest()
        }
    }
}

