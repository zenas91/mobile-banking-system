package com.proemion.machine.mobilebanking.adapter

import android.content.Context
import androidx.core.hardware.fingerprint.FingerprintManagerCompat.*

object FingerprintUtils {

    /**
     * Condition I: Check if the device has fingerprint sensors.
     * Note: If you marked android.hardware.fingerprint as something that
     * your app requires (android:required="true"), then you don't need
     * to perform this check.
     *
     */
    @SuppressWarnings("deprecation")
    fun isHardwareSupported(context: Context?): Boolean {
        val fingerprintManager = from(context!!)
        return fingerprintManager.isHardwareDetected
    }

    /**
     * Condition II: Fingerprint authentication can be matched with a
     * registered fingerprint of the user. So we need to perform this check
     * in order to enable fingerprint authentication
     *
     */
    @SuppressWarnings("deprecation")
    fun isEnrolledFingerprintAvailable(context: Context?): Boolean {
        val fingerprintManager = from(context!!)
        return fingerprintManager.hasEnrolledFingerprints()
    }
}