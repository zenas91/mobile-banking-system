/***************************************************************************************************
 ****************************Created by Chukwuebuka Ezelu on 12/09/2020*****************************
 **************************************************************************************************/
package com.proemion.machine.mobilebanking.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AddressSearch {
    @SerializedName("results")
    @Expose
    var addresses: List<Address>? = null
}