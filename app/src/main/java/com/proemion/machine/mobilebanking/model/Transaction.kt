/***************************************************************************************************
 ****************************Created by Chukwuebuka Ezelu on 13/09/2020*****************************
 **************************************************************************************************/
package com.proemion.machine.mobilebanking.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Transaction {

    @SerializedName("ref")
    @Expose
    var ref: String? = null

    @SerializedName("amount")
    @Expose
    var amount: Int? = null

    @SerializedName("debit")
    @Expose
    var debit: String? = null

    @SerializedName("credit")
    @Expose
    var credit: String? = null

    @SerializedName("balbeforedebit")
    @Expose
    var balbeforedebit: Int? = null

    @SerializedName("balafterdebit")
    @Expose
    var balafterdebit: Int? = null

    @SerializedName("balbeforecredit")
    @Expose
    var balbeforecredit: Int? = null

    @SerializedName("balaftercredit")
    @Expose
    var balaftercredit: Int? = null

    @SerializedName("created")
    @Expose
    var created: String? = null

    @SerializedName("modified")
    @Expose
    var modified: String? = null

    @SerializedName("enabled")
    @Expose
    var enabled: Boolean? = null

}