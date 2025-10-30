package com.bagmanovam.rikiandmorti.data.internet

import com.bagmanovam.rikiandmorti.data.model.RikMortiHeroDto
import com.bagmanovam.rikiandmorti.data.model.RikMortiHeroesDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RikMortiApi {

    @GET("api/character/")
    suspend fun getRikMortiHeroes(@Query("page") page: Int): Response<RikMortiHeroesDto>

    @GET("api/character/{id}")
    suspend fun getRikMortiHero(@Path("id") itemId: Int): Response<RikMortiHeroDto>
}