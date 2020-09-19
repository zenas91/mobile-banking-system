/***************************************************************************************************
 ****************************Created by Chukwuebuka Ezelu on 10/09/2020*****************************
 **************************************************************************************************/
package com.proemion.machine.mobilebanking.model
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Account {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("iban")
    @Expose
    var iban: String? = null

    @SerializedName("number")
    @Expose
    var number: String? = null

    @SerializedName("owner")
    @Expose
    var owner: String? = null

    @SerializedName("act_type")
    @Expose
    var actType: String? = null

    @SerializedName("balance")
    @Expose
    var balance: Double? = null

    @SerializedName("overdraft")
    @Expose
    var overdraft: Boolean? = null

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