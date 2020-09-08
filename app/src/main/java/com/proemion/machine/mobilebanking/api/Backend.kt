/***************************************************************************************************
 ****************************Created by Chukwuebuka Ezelu on 08/09/2020*****************************
 **************************************************************************************************/
package com.proemion.machine.mobilebanking.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class Backend {
    companion object{
        private const val BASE_URL = "http://127.0.0.1:8000/:8000"
        private val httpClient= OkHttpClient.Builder().build()

        private val retrofitBuilder1 = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(httpClient).build()

        private val retrofitBuilder2 = Retrofit.Builder()
            .baseUrl(BASE_URL).addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).client(httpClient).build()

        private val retrofitApi1 = retrofitBuilder1.create(BackendAPI::class.java)
        private val retrofitApi2 = retrofitBuilder2.create(BackendAPI::class.java)

        fun getRetrofitApi1(): BackendAPI? {
            return retrofitApi1
        }

        fun getRetrofitApi2(): BackendAPI? {
            return retrofitApi2
        }
    }
}