package com.proemion.machine.mobilebanking

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.proemion.machine.mobilebanking.api.Backend
import com.proemion.machine.mobilebanking.model.Account
import com.proemion.machine.mobilebanking.model.User
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class RegisterActivity : AppCompatActivity() {

    val mContext  = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        showEnterAnimation()

        fab.setOnClickListener { animateRevealClose() }
        bt_go.setOnClickListener { createUser() }

        ArrayAdapter.createFromResource(this, R.array.account_types,
            R.layout.selected_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
            account_type.adapter = adapter
        }

    }

    private fun fieldsAreValidated(firstname: String, lastname: String, username: String,
                                   email:String, password:String, repeatPassword:String): Boolean {

        if (firstname.isBlank() || lastname.isBlank() || username.isBlank() || email.isBlank() ||
                    password.isBlank() || repeatPassword.isBlank() || et_balance.text.isNullOrBlank()){
            Toast.makeText(mContext, "All fields must be completed", Toast.LENGTH_SHORT).show()
            return false
        }
        else if (password.length < 8){
            Toast.makeText(mContext, "Password must contain at least 8 characters", Toast.LENGTH_SHORT).show()
            return false
        }
        else if (password != repeatPassword){
            Toast.makeText(mContext, "Password do not match Repeat password", Toast.LENGTH_SHORT).show()
            return false
        }
        else if (et_balance.text.toString().toInt() == 0){
            Toast.makeText(mContext, "Opening balance cannot be 0", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun createUser() {

        val firstname = et_first_name.text.toString()
        val lastname = et_last_name.text.toString()
        val username = et_username.text.toString()
        val email = et_email.text.toString()
        val password = et_password.text.toString()
        val repeatPassword = et_repeat_password.text.toString()

        if (fieldsAreValidated(firstname, lastname, username, email, password, repeatPassword)) {
            val call: Call<User?> = Backend.getRetrofitApi1()!!
                .createUser(firstname, lastname, username, email, password)

            call.enqueue(object : Callback<User?> {
                override fun onResponse(call: Call<User?>, response: Response<User?>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        createAccount(result?.id!!)
                    }
                }

                override fun onFailure(call: Call<User?>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(mContext, "Failed ${t.localizedMessage}", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }
    }

    private fun createAccount(ownerID:Int){
        val owner = "http://127.0.0.1:8000/users/$ownerID/"
        val (iban, accNumber) = generateIBANAndAccNumber()
        val balance = et_balance.text.toString().toLong()
        val overdraft = switch_overdraft.isChecked
        val accType = account_type.selectedItem as String

        val call: Call<Account?> = Backend.getRetrofitApi1()!!
            .createAccount(iban, accNumber, owner, accType, balance, overdraft)

        call.enqueue(object : Callback<Account?> {
            override fun onResponse(call: Call<Account?>, response: Response<Account?>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Toast.makeText(mContext, "Account ${result?.iban} was created successfully",
                        Toast.LENGTH_SHORT).show()
                    clearFields()
                } else {
                    Toast.makeText(mContext, "Unknown error! unable to create account",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Account?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun generateIBANAndAccNumber(): Pair<String,String>{
        val random16Values = List(16) { Random.nextInt(0, 10) }
        val random8Values = List(8) { Random.nextInt(0, 10) }
        var newIBAN = "BD"
        var newAccNumber = "64"
        random16Values.forEach { newIBAN += it }
        random8Values.forEach { newAccNumber += it }
        return Pair(newIBAN, newAccNumber)
    }

    private fun clearFields(){
        et_first_name.text = null
        et_last_name.text = null
        et_username.text = null
        et_email.text = null
        et_password.text = null
        et_repeat_password.text = null
        et_balance.text = null
        switch_overdraft.isChecked = false
        account_type.setSelection(0)
    }

    fun animateRevealShow() {
        val mAnimator = ViewAnimationUtils.createCircularReveal( cv_add,
            cv_add.width / 2,0, (fab.width / 2).toFloat(), cv_add.height.toFloat() )
        mAnimator.duration = 500
        mAnimator.interpolator = AccelerateInterpolator()
        mAnimator.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationStart(animation: Animator) {
                cv_add.visibility = View.VISIBLE
                super.onAnimationStart(animation)
            }
        })
        mAnimator.start()
    }

    private fun animateRevealClose() {
        val mAnimator = ViewAnimationUtils.createCircularReveal( cv_add,
            cv_add.width / 2, 0, cv_add.height.toFloat(), (fab.width / 2).toFloat())
        mAnimator.duration = 500
        mAnimator.interpolator = AccelerateInterpolator()
        mAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                cv_add.visibility = View.INVISIBLE
                super.onAnimationEnd(animation)
                fab.setImageResource(R.drawable.ic_sign_up)
                super@RegisterActivity.onBackPressed()
            }
        })
        mAnimator.start()
    }

    private fun showEnterAnimation() {
        val transition = TransitionInflater.from(this)
                .inflateTransition(R.transition.fabtransition)
        window.sharedElementEnterTransition = transition
        transition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {
                cv_add.visibility = View.GONE
            }
            override fun onTransitionEnd(transition: Transition) {
                transition.removeListener(this)
                animateRevealShow()
            }
            override fun onTransitionCancel(transition: Transition) {}
            override fun onTransitionPause(transition: Transition) {}
            override fun onTransitionResume(transition: Transition) {}
        })
    }

    override fun onBackPressed() { animateRevealClose() }

}