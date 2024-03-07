package com.ksalauyou.stockpricegraphic.data

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("aggs/ticker/AAPL/range/{timeFrame}/2022-01-09/2023-01-09?adjusted=true&sort=desc&limit=50000&apiKey=Cp7ge7mELiipsNTfQi0r94EPP3PXMuE0")
    suspend fun loadBars(
        @Path("timeFrame") timeFrame: String
    ): Result
}