package com.proemion.machine.mobilebanking

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.proemion.machine.mobilebanking.StaticComponent.StaticConfig
import com.proemion.machine.mobilebanking.api.Backend
import com.proemion.machine.mobilebanking.model.AccountSearch
import com.proemion.machine.mobilebanking.model.AddressSearch
import com.proemion.machine.mobilebanking.model.User
import com.proemion.machine.mobilebanking.ui.AddressActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    var ownerID by Delegates.notNull<Int>()
    var ownerAccountId by Delegates.notNull<Int>()

    lateinit var ownerName:String
    lateinit var ownerEmail:String
    lateinit var ownerIBAN:String
    lateinit var ownerACCNum:String
    lateinit var ownerBalance:String
    lateinit var username:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        ownerID = intent.getIntExtra(StaticConfig.OWNER_ID, 0)
        getUserAccounts(ownerID)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        nav_sign_out.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }

    private fun getUserAccounts(ownerID:Int){
        val call: Call<AccountSearch> = Backend.getRetrofitApi1()!!.getUserAccounts(ownerID)

        call.enqueue(object : Callback<AccountSearch> {
            override fun onResponse(call: Call<AccountSearch>, response: Response<AccountSearch>) {
                if (response.isSuccessful) {
                    val result = response.body()

                    if (!result?.accounts.isNullOrEmpty()){
                        val accType = "${result?.accounts?.get(0)?.actType} ACCOUNT"
                        account_type.text = accType
                        account_number.text = result?.accounts?.get(0)?.number
                        ownerACCNum = result?.accounts?.get(0)?.number.toString()
                        iban_number.text = result?.accounts?.get(0)?.iban
                        ownerIBAN = result?.accounts?.get(0)?.iban.toString()
                        balance.text = result?.accounts?.get(0)?.balance.toString()
                        ownerBalance = result?.accounts?.get(0)?.balance.toString()
                        ownerAccountId = result?.accounts?.get(0)?.id!!
                        getUser(ownerID)
                    }

                }
            }
            override fun onFailure(call: Call<AccountSearch>, t: Throwable) { t.printStackTrace() }
        })
    }

    private fun getUser(ownerID:Int){
        val call: Call<User> = Backend.getRetrofitApi1()!!.getUser(ownerID)

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val name = "${result?.firstName} ${result?.lastName}"
                    ownerName = name
                    owner_name.text = name
                    owner_email.text = result?.email
                    ownerEmail = result?.email.toString()
                    username = result?.username.toString()
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) { t.printStackTrace() }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}