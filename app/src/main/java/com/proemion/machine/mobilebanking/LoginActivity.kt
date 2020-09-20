package com.proemion.machine.mobilebanking

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.proemion.machine.mobilebanking.StaticComponent.StaticConfig.OWNER_ID
import com.proemion.machine.mobilebanking.adapter.FingerprintUtils.isEnrolledFingerprintAvailable
import com.proemion.machine.mobilebanking.adapter.FingerprintUtils.isHardwareSupported
import com.proemion.machine.mobilebanking.api.Backend
import com.proemion.machine.mobilebanking.model.AddressSearch
import com.proemion.machine.mobilebanking.model.Login
import com.proemion.machine.mobilebanking.ui.AddressActivity
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors
import androidx.biometric.BiometricPrompt

class LoginActivity : AppCompatActivity() {
    val mContext = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (!isHardwareSupported(this)){
            AlertDialog.Builder(mContext)
                .setTitle(getString(R.string.fingerprint_header))
                .setMessage(getString(R.string.fingerprint_message))
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        if (!isEnrolledFingerprintAvailable(this)){
            AlertDialog.Builder(mContext)
                .setTitle(getString(R.string.fingerprint_header))
                .setMessage(getString(R.string.fingerprint_enrolment))
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        fab.setOnClickListener {
            window.exitTransition = null
            window.enterTransition = null

            val options: ActivityOptions =
                ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.transitionName)
            startActivity(Intent(this, RegisterActivity::class.java), options.toBundle())
        }

        bt_go.setOnClickListener {
            attemptLogin()
        }
    }

    private fun attemptLogin() {

        // Reset errors.
        et_username.error = null
        et_password.error = null

        // Store values at the time of the login attempt.
        val myUsername: String = et_username.text.toString()
        val password: String = et_password.text.toString()
        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password.trim()) && !isPasswordValid(password)) {
            et_password.error = getString(R.string.error_invalid_password)
            focusView = et_password
            cancel = true
        }

        // Check if username is entered.
        if (TextUtils.isEmpty(myUsername.trim())) {
            et_username.error = getString(R.string.error_field_required)
            focusView = et_username
            cancel = true
        }
        // Check if password is entered.
        if (!TextUtils.isEmpty(myUsername.trim()) && TextUtils.isEmpty(password.trim())) {
            et_password.error = getString(R.string.error_field_required)
            focusView = et_password
            cancel = true
        }
        if (cancel) {
            focusView!!.requestFocus()
        } else {
            val call: Call<Login?> = Backend.getRetrofitApi1()!!.authenticate(myUsername, password)
            call.enqueue(object : Callback<Login?> { override fun onResponse( call: Call<Login?>,
                    response: Response<Login?> ) {
                    if (response.isSuccessful) {
                        val res: Login? = response.body()
                        biometricLogin(res?.id!!)
                    }
                    else{
                        Toast.makeText(mContext, "Login failed, please check your credentials", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure( call: Call<Login?>, t: Throwable ) {
                    Log.d("Spark", t.toString())
                    Toast.makeText(this@LoginActivity, "Fail", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


    private fun biometricLogin(ownerID:Int) {

        /**Create a thread pool with a single thread */
        val newExecutor = Executors.newSingleThreadExecutor()

        /**Start listening for authentication events */
        val myBiometricPrompt = BiometricPrompt(this, newExecutor,
            object : BiometricPrompt.AuthenticationCallback() {
                override
                        /**onAuthenticationError is called when a fatal error occurs */
                        /**onAuthenticationError is called when a fatal error occurs */
                fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    } else {

                    }
                }
                /**onAuthenticationSucceeded is called when a fingerprint is matched successfully */
                /**onAuthenticationSucceeded is called when a fingerprint is matched successfully */
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)

                    checkAddress(ownerID)
                }
            })
        /** This creates a BiometricPrompt instance */
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            /**customize the dialog*/
            .setTitle("Fingerprint Authentication")
            .setDescription("")
            .setNegativeButtonText("Cancel")
            .build()

        myBiometricPrompt.authenticate(promptInfo)

    }


    private fun checkAddress(ownerID:Int){
        val call: Call<AddressSearch> = Backend.getRetrofitApi1()!!.getUserAddresses(ownerID)

        call.enqueue(object : Callback<AddressSearch> {
            override fun onResponse(call: Call<AddressSearch>, response: Response<AddressSearch>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result?.addresses.isNullOrEmpty()) {
                        val intent = Intent(mContext, AddressActivity::class.java)
                        intent.putExtra(OWNER_ID, ownerID)
                        startActivity(intent)
                    }else{
                        val intent = Intent(mContext, MainActivity::class.java)
                        intent.putExtra(OWNER_ID, ownerID)
                        startActivity(intent)
                    }
                }
            }
            override fun onFailure(call: Call<AddressSearch>, t: Throwable) { t.printStackTrace() }
        })
    }

    private fun isPasswordValid(password: String): Boolean { return password.length > 7 }

}