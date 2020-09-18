package com.proemion.machine.mobilebanking.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.proemion.machine.mobilebanking.R
import com.proemion.machine.mobilebanking.StaticComponent.StaticConfig
import com.proemion.machine.mobilebanking.api.Backend
import com.proemion.machine.mobilebanking.model.AddressSearch
import kotlinx.android.synthetic.main.activity_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class ProfileActivity : AppCompatActivity() {

    var ownerID by Delegates.notNull<Int>()
    var ownerAccountId by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        ownerID = intent.getIntExtra(StaticConfig.OWNER_ID, 0)
        ownerAccountId = intent.getIntExtra(StaticConfig.OWNER_ACCOUNT_ID, 0)

        et_name.setText(intent.getStringExtra(StaticConfig.OWNER_NAME)!!)
        et_email.setText(intent.getStringExtra(StaticConfig.OWNER_EMAIL)!!)
        et_username.setText(intent.getStringExtra(StaticConfig.OWNER_USERNAME)!!)

        getAddress(ownerID)

    }

    private fun getAddress(ownerID:Int){
        val call: Call<AddressSearch> = Backend.getRetrofitApi1()!!.getUserAddresses(ownerID)

        call.enqueue(object : Callback<AddressSearch> {
            override fun onResponse(call: Call<AddressSearch>, response: Response<AddressSearch>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    et_street.setText(result?.addresses?.get(0)?.street)
                    et_house_number.setText(result?.addresses?.get(0)?.houseNumber.toString())
                    et_postcode.setText(result?.addresses?.get(0)?.postcode.toString())
                    et_city.setText(result?.addresses?.get(0)?.city)
                    et_state.setText(result?.addresses?.get(0)?.state)
                    et_country.setText(result?.addresses?.get(0)?.country)
                }
            }
            override fun onFailure(call: Call<AddressSearch>, t: Throwable) { t.printStackTrace() }
        })
    }
}