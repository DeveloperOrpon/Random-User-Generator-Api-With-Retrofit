package com.example.apiwithrecycleview.server

import com.example.apiwithrecycleview.DataClass
import retrofit2.Call
import retrofit2.http.GET

interface ServerApi {
    @GET("api")
    fun getUser(): Call<DataClass>
}