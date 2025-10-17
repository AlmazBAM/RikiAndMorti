package com.bagmanovam.rikiandmorti.data.internet

import com.bagmanovam.rikiandmorti.data.model.RikMortiHeroDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RikMortiApi {

    @GET("character/1,2,3,4,5,6,7,8")
    suspend fun getRikMortiHeroes(): Response<List<RikMortiHeroDto>>
}