package com.okidoki.x_modechallenge

import android.content.Context


interface RetrieveLocationContract {

    interface View {

        fun showRepeatingEventSetToast()

        fun showAccessGrantedToast()

        fun showAccessDeniedToast()

        fun showLocationRationale()

        fun sendLocationPermissionRequest()
    }

    interface UserActionsListener {

        fun setRepeatingLocationAlarm(context: Context)
    }

}