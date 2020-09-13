/***************************************************************************************************
 ****************************Created by Chukwuebuka Ezelu on 13/09/2020*****************************
 **************************************************************************************************/
package com.proemion.machine.mobilebanking.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class TransactionSearch {

    @SerializedName("results")
    @Expose
    var transactions: List<Transaction>? = null

}