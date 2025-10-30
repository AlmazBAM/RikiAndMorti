package com.bagmanovam.rikiandmorti.data.model

data class RikMortiHeroesDto(
    val info: InfoDto,
    val results: List<RikMortiHeroDto>
)

data class InfoDto(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)
