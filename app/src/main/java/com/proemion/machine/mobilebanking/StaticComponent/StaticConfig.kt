/***************************************************************************************************
 ****************************Created by Chukwuebuka Ezelu on 01/08/2020*****************************
 **************************************************************************************************/
package com.proemion.machine.mobilebanking.StaticComponent

object StaticConfig {

    const val REQUEST_CODE_REGISTER = 2000
    const val OWNER_ID = "Owner_ID"
    const val OWNER_ACCOUNT_ID = "Owner_Account_ID"
    const val OWNER_NAME = "Owner_Name"
    const val OWNER_EMAIL = "Owner_Email"
    const val OWNER_IBAN = "Owner_IBAN"
    const val OWNER_ACC_NUM = "Owner_Account_Number"
    const val OWNER_ACC_BALANCE = "Owner_Account_Balance"
    const val OWNER_USERNAME = "Owner_Username"

    fun getOwnerUrl(ownerId: Int):String {
        return "http://10.0.2.2:8000/users/$ownerId/"
    }

    fun getAccountUrl(accountId: Int):String {
        return "http://10.0.2.2:8000/accounts/$accountId/"
    }

    fun getAccountIdFromURL(url:String):Int {
        return url.replace("http://10.0.2.2:8000/accounts/", "").replace("/", "").toInt()
    }
}