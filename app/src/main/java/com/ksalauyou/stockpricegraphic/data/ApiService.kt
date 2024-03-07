package com.ksalauyou.stockpricegraphic.data

import retrofit2.http.GET

interface ApiService {

    @GET("aggs/ticker/AAPL/range/1/hour/2022-01-09/2023-01-09?adjusted=true&sort=asc&limit=50000&apiKey=Cp7ge7mELiipsNTfQi0r94EPP3PXMuE0")
    suspend fun loadBars(): Result
}