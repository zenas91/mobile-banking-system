/***************************************************************************************************
 ****************************Created by Chukwuebuka Ezelu on 08/09/2020*****************************
 **************************************************************************************************/
package com.proemion.machine.mobilebanking.api

import com.proemion.machine.mobilebanking.model.Account
import com.proemion.machine.mobilebanking.model.Login
import com.proemion.machine.mobilebanking.model.User
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface BackendAPI {

    @FormUrlEncoded
    @POST("/auth/")
    fun authenticate(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<Login?>

    @FormUrlEncoded
    @POST("/users/")
    fun createUser(
        @Field("first_name") firstName: String,
        @Field("last_name") lastName: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("is_active") isActive: Boolean = true
    ): Call<User?>

    @FormUrlEncoded
    @POST("/accounts/")
    fun createAccount(
        @Field("iban") iban: String,
        @Field("number") number: String,
        @Field("owner") owner: String,
        @Field("act_type") accType: String,
        @Field("balance") balance: Long,
        @Field("overdraft") overdraft: Boolean,
        @Field("enabled") enabled: Boolean = true
    ): Call<Account?>


}