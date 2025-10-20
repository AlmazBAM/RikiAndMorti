package com.bagmanovam.rikiandmorti.data.internet

import com.bagmanovam.rikiandmorti.data.model.RikMortiHeroDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RikMortiApi {

    @GET("api/character/1,2,3,4")
    suspend fun getRikMortiHeroes(): Response<List<RikMortiHeroDto>>

    @GET("api/character/{id}")
    suspend fun getRikMortiHero(@Path("id") itemId: Int): Response<RikMortiHeroDto>
}