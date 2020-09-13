package com.proemion.machine.mobilebanking.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.proemion.machine.mobilebanking.MainActivity
import com.proemion.machine.mobilebanking.R
import com.proemion.machine.mobilebanking.StaticComponent.StaticConfig
import com.proemion.machine.mobilebanking.StaticComponent.StaticConfig.OWNER_ID
import com.proemion.machine.mobilebanking.api.Backend
import com.proemion.machine.mobilebanking.model.Account
import com.proemion.machine.mobilebanking.model.Address
import kotlinx.android.synthetic.main.activity_address.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field

class AddressActivity : AppCompatActivity() {
    val mContext = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        val ownerID = intent.getIntExtra(OWNER_ID, 0)
        bt_address.setOnClickListener { (createAddress(ownerID)) }

    }

    private fun createAddress(ownerID:Int){
        val owner = StaticConfig.getOwnerUrl(ownerID)
        val street = et_street.text.toString()
        val houseNumber = et_house_number.text.toString().toInt()
        val postcode = et_postcode.text.toString().toInt()
        val city = et_city.text.toString()
        val state = et_state.text.toString()
        val country = et_country.text.toString()

        val call: Call<Address?> = Backend.getRetrofitApi1()!!
            .createAddress(owner, street, houseNumber, postcode, city, state, country)

        call.enqueue(object : Callback<Address?> {
            override fun onResponse(call: Call<Address?>, response: Response<Address?>) {
                if (response.isSuccessful) {
                    Toast.makeText(mContext, "Address saved successfully", Toast.LENGTH_SHORT)
                        .show()
                    et_street.text = null
                    et_house_number.text = null
                    et_postcode.text = null
                    et_city.text = null
                    et_state.text = null
                    et_country.text = null

                    val intent = Intent(mContext, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(mContext, "Unknown error! unable to create account",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Address?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}