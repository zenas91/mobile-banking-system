package com.proemion.machine.mobilebanking

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proemion.machine.mobilebanking.ui.AddressActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        fab.setOnClickListener {
            window.exitTransition = null
            window.enterTransition = null

            val options: ActivityOptions =
                ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.transitionName)
            startActivity(Intent(this, RegisterActivity::class.java), options.toBundle())
        }

        val num = 1
        bt_go.setOnClickListener {

            if(num == 1){
                val intent = Intent(this, AddressActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        }
    }

}