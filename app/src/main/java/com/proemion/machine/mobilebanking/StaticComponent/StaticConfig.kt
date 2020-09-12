/***************************************************************************************************
 ****************************Created by Chukwuebuka Ezelu on 01/08/2020*****************************
 **************************************************************************************************/
package com.proemion.machine.mobilebanking.StaticComponent

object StaticConfig {

    const val REQUEST_CODE_REGISTER = 2000

    fun getOwnerUrl(ownerId: Int):String {
        return "http://127.0.0.1:8000/users/$ownerId/"
    }
}