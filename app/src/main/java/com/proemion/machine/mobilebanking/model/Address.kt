/***************************************************************************************************
 ****************************Created by Chukwuebuka Ezelu on 12/09/2020*****************************
 **************************************************************************************************/
package com.proemion.machine.mobilebanking.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Address {

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("owner")
    @Expose
    var owner: String? = null

    @SerializedName("street")
    @Expose
    var street: String? = null

    @SerializedName("housenumber")
    @Expose
    var houseNumber: Int? = null

    @SerializedName("postcode")
    @Expose
    var postcode: Int? = null

    @SerializedName("city")
    @Expose
    var city: String? = null

    @SerializedName("state")
    @Expose
    var state: String? = null

    @SerializedName("country")
    @Expose
    var country: String? = null

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