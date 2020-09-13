/***************************************************************************************************
 ****************************Created by Chukwuebuka Ezelu on 08/09/2020*****************************
 **************************************************************************************************/
package com.proemion.machine.mobilebanking.api

import com.proemion.machine.mobilebanking.model.*
import retrofit2.Call
import retrofit2.http.*


interface BackendAPI {

    @FormUrlEncoded
    @POST("/api-token-auth/")
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


    @FormUrlEncoded
    @POST("/addresses/")
    fun createAddress(
        @Field("owner") owner: String,
        @Field("street") street: String,
        @Field("housenumber") houseNumber: Int,
        @Field("postcode") postcode: Int,
        @Field("city") city: String,
        @Field("state") state: String,
        @Field("country") country: String,
        @Field("enabled") enabled: Boolean = true
    ): Call<Address?>

    @FormUrlEncoded
    @POST("/transactions/")
    fun processTransaction(
        @Field("ref") ref: String,
        @Field("amount") amount: Int,
        @Field("debit") debit: String,
        @Field("credit") credit: String,
        @Field("balbeforedebit") balbeforedebit: Int?,
        @Field("balafterdebit") balafterdebit: Int?,
        @Field("balbeforecredit") balbeforecredit: Int?,
        @Field("balaftercredit") balaftercredit: Int?,
        @Field("enabled") enabled: Boolean = true
    ): Call<Transaction?>

    @FormUrlEncoded
    @PUT("/accounts/{accountId}/")
    fun updateAccount(
        @Field("iban") iban: String,
        @Field("number") number: String,
        @Field("owner") owner: String,
        @Field("act_type") accType: String,
        @Field("balance") balance: Int,
        @Field("overdraft") overdraft: Boolean,
        @Field("enabled") enabled: Boolean = true,
        @Path("accountId") accountId: Int
    ): Call<Account?>

    @GET("/addressSearch/")
    fun getUserAddresses(@Query("owner") ownerId: Int): Call<AddressSearch>

    @GET("/accountSearch/")
    fun getUserAccounts(@Query("owner") ownerId: Int): Call<AccountSearch>

    @GET("/accountSearch/")
    fun getUserAccounts(@QueryMap options: Map<String, String>): Call<AccountSearch>

    @GET("/users/{ownerId}")
    fun getUser(@Path("ownerId") ownerId: Int): Call<User>

    @GET("/search/")
    fun getUserTransactions(@QueryMap options: Map<String, String>): Call<TransactionSearch>

}