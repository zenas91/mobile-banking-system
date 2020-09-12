/***************************************************************************************************
 ****************************Created by Chukwuebuka Ezelu on 10/09/2020*****************************
 **************************************************************************************************/
package com.proemion.machine.mobilebanking.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Login {
    @SerializedName("token")
    @Expose
    var token: String? = null

    @SerializedName("user_id")
    @Expose
    var userId: Int? = null

}