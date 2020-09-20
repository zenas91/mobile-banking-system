package com.proemion.machine.mobilebanking.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.proemion.machine.mobilebanking.MainActivity
import com.proemion.machine.mobilebanking.R
import com.proemion.machine.mobilebanking.StaticComponent.StaticConfig
import com.proemion.machine.mobilebanking.StaticComponent.StaticConfig.getAccountUrl
import com.proemion.machine.mobilebanking.api.Backend
import com.proemion.machine.mobilebanking.model.Account
import com.proemion.machine.mobilebanking.model.AccountSearch
import com.proemion.machine.mobilebanking.model.Transaction
import com.proemion.machine.mobilebanking.model.User
import kotlinx.android.synthetic.main.activity_transaction.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random


class TransactionActivity : AppCompatActivity() {

    val mContext = this
    private var balbeforedebit:Int? = null
    private var balafterdebit:Int? = null
    private var balbeforecredit:Int? = null
    private var balaftercredit:Int? = null
    private var debit:String? = null
    private var credit:String? = null

    private var debitAccount: Account? = null
    private var creditAccount: Account? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        val ownerID = intent.getIntExtra(StaticConfig.OWNER_ID, 0)
        getUserAccounts(ownerID)
        viewListener()
        bt_process.setOnClickListener { processTransaction() }
    }


    private fun viewListener(){
        et_iban.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (s.toString().length == 18) { getUserAccounts("iban", s.toString()) }
            } })

        et_amount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                balaftercredit = if (count != 0 && balbeforecredit != null){
                    balbeforecredit!! + s.toString().toInt() } else balbeforecredit

                balafterdebit = if (count != 0 && balbeforedebit != null){
                    balbeforedebit!! - s.toString().toInt() } else balbeforedebit
            }

            override fun afterTextChanged(s: Editable) {}
        })

    }

    private fun processTransaction(){
        if (et_iban.text.isNullOrBlank() || et_account_owner.text.isNullOrBlank() ||
            et_amount.text.isNullOrBlank()){
            Toast.makeText(mContext, "Please provide valid information for all field",
                Toast.LENGTH_SHORT).show()
            return
        }
        else if (et_iban.text!!.length != 18){
            Toast.makeText(mContext, "Please provide a IBAN", Toast.LENGTH_SHORT).show()
            return
        }
        else if (balbeforecredit == null){
            Toast.makeText(mContext, "Please enter valid IBAN before entering amount",
                Toast.LENGTH_SHORT).show()
            return
        }
        else if (debit == credit){
            Toast.makeText(mContext, "You cannot transfer to yourself",
                Toast.LENGTH_SHORT).show()
            return
        }
        else if (balafterdebit!! < 0){
            Toast.makeText(mContext, "Entered amount is greater than your balance",
                Toast.LENGTH_SHORT).show()
            return
        }
        else{
            val call: Call<Transaction?> = Backend.getRetrofitApi1()!!.processTransaction(
                generateTransactionReference(), et_amount.text.toString().toInt(), debit!!,
                credit!!, balbeforedebit, balafterdebit, balbeforecredit, balaftercredit)

            call.enqueue(object : Callback<Transaction?> {
                override fun onResponse(call: Call<Transaction?>, response: Response<Transaction?>) {
                    if (response.isSuccessful) {
                        updateAccounts(debitAccount!!, balafterdebit!!)
                        updateAccounts(creditAccount!!, balaftercredit!!)

                        AlertDialog.Builder(mContext)
                            .setTitle("Transaction Successful")
                            .setMessage("The Transfer transaction to ${creditAccount?.iban} was successful")
                            .setPositiveButton(android.R.string.ok) { _, _ ->
                                val ownerID = mContext.intent.getIntExtra(StaticConfig.OWNER_ID, 0)
                                val intent = Intent(mContext, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                intent.putExtra(StaticConfig.OWNER_ID, ownerID)
                                startActivity(intent)
                                finish()
                            }
                            .show()


                    } else { Toast.makeText(mContext, "Unknown error! unable to process " +
                            "Transaction", Toast.LENGTH_SHORT).show() }
                }

                override fun onFailure(call: Call<Transaction?>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }

    private fun generateTransactionReference(): String{
        val randomValues = List(8) { Random.nextInt(0, 10) }
        var newTransaction = "RTQ"
        randomValues.forEach { newTransaction += it }
        return newTransaction
    }

    private fun updateAccounts(account: Account, amount: Int){
        val call: Call<Account?> = Backend.getRetrofitApi1()!!.updateAccount(account.iban!!,
        account.number!!, account.owner!!, account.actType!!, amount, account.overdraft!!,
        account.enabled!!, account.id!!)
        call.enqueue(object : Callback<Account?> {
            override fun onResponse(call: Call<Account?>, response: Response<Account?>) {
                if (response.isSuccessful) { }
            }
            override fun onFailure(call: Call<Account?>, t: Throwable) { t.printStackTrace() }
        })
    }

    private fun getUserAccounts(ownerID:Int){
        val call: Call<AccountSearch> = Backend.getRetrofitApi1()!!.getUserAccounts(ownerID)
        call.enqueue(object : Callback<AccountSearch> {
            override fun onResponse(call: Call<AccountSearch>, response: Response<AccountSearch>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (!result?.accounts.isNullOrEmpty()){
                        balbeforedebit = result?.accounts?.get(0)?.balance?.toInt()
                        debit = result?.accounts?.get(0)?.id?.let { getAccountUrl(it) }
                        debitAccount = result?.accounts?.get(0)
                    }

                }
            }
            override fun onFailure(call: Call<AccountSearch>, t: Throwable) { t.printStackTrace() }
        })
    }

    private fun getUserAccounts(key:String, value:String){
        val option = mapOf(key to value)
        val call: Call<AccountSearch> = Backend.getRetrofitApi1()!!.getUserAccounts(option)

        call.enqueue(object : Callback<AccountSearch> {
            override fun onResponse(call: Call<AccountSearch>, response: Response<AccountSearch>) {
                if (response.isSuccessful) {
                    val result = response.body()

                    if (!result?.accounts.isNullOrEmpty()){
                        balbeforecredit = result?.accounts?.get(0)?.balance?.toInt()
                        credit = result?.accounts?.get(0)?.id?.let { getAccountUrl(it) }
                        val ownerID = result?.accounts?.get(0)?.owner
                        val res = ownerID?.
                                    replace("http://ec2-3-92-202-42.compute-1.amazonaws.com/users/", "")?.
                                    replace("/", "")
                        res?.toInt()?.let { getUser(it) }
                        creditAccount = result?.accounts?.get(0)
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
                    et_account_owner.setText(name)
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) { t.printStackTrace() }
        })
    }

}