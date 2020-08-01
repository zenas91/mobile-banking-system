package com.proemion.machine.mobilebanking

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun clickRegisterLayout(view: View?) {
        window.exitTransition = null
        window.enterTransition = null

        val options: ActivityOptions =
            ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.transitionName)
        startActivity(Intent(this, RegisterActivity::class.java), options.toBundle())
    }

}