/***************************************************************************************************
 ****************************Created by Chukwuebuka Ezelu on 13/09/2020*****************************
 **************************************************************************************************/
package com.proemion.machine.mobilebanking.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.proemion.machine.mobilebanking.R
import com.proemion.machine.mobilebanking.StaticComponent.StaticConfig
import com.proemion.machine.mobilebanking.StaticComponent.StaticConfig.getAccountIdFromURL
import com.proemion.machine.mobilebanking.StaticComponent.StaticConfig.getAccountUrl
import com.proemion.machine.mobilebanking.api.Backend
import com.proemion.machine.mobilebanking.model.AccountSearch
import com.proemion.machine.mobilebanking.model.Transaction
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionAdapter (private val mContext: Context, private val ownerAccountId: Int,
                                    private val transactionList: List<Transaction>)
    : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>(){

    private var mLastClickTime: Long = 0
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): TransactionViewHolder {
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.transaction_custom_view, null)
        return TransactionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    inner class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var referenceView: TextView = view.findViewById(R.id.reference_view)
        var transactionTypeView: TextView = view.findViewById(R.id.transaction_type_view)
        var ibanView: TextView = view.findViewById(R.id.iban_view)
        var amountView: TextView = view.findViewById(R.id.amount_view)
        var dateView: TextView = view.findViewById(R.id.date_view)
        var ibanTitleView: TextView = view.findViewById(R.id.iban_title_view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.referenceView.text = transaction.ref
        holder.amountView.text = transaction.amount.toString()
        holder.dateView.text = transaction.created?.replace("T", " ")?.
                    replaceAfter(".", "")?.replace(".", "")
        holder.transactionTypeView.text = if (transaction.debit == getAccountUrl(ownerAccountId))
                                                "Debit" else "Credit"
        if (transaction.credit == getAccountUrl(ownerAccountId)) {
            holder.ibanTitleView.text = "From: "
            getAccounts(ownerAccountId, holder.ibanView)
        }
        else{
            holder.ibanTitleView.text = "To: "
            getAccounts(getAccountIdFromURL(transaction.credit!!), holder.ibanView)
        }
    }

    private fun getAccounts(ownerID:Int, view: TextView){
        val call: Call<AccountSearch> = Backend.getRetrofitApi1()!!.getUserAccounts(ownerID)
        call.enqueue(object : Callback<AccountSearch> {
            override fun onResponse(call: Call<AccountSearch>, response: Response<AccountSearch>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    view.text = result?.accounts?.get(0)?.iban

                }
            }
            override fun onFailure(call: Call<AccountSearch>, t: Throwable) { t.printStackTrace() }
        })
    }
}