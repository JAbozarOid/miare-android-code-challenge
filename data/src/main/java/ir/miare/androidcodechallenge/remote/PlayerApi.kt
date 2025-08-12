package ir.miare.androidcodechallenge.remote

import retrofit2.http.GET

interface PlayerApi {

    @GET("list")
    suspend fun getAll(): List<FakeData>
}