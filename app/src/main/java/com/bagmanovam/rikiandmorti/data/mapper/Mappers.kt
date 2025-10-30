package com.bagmanovam.rikiandmorti.data.mapper

import com.bagmanovam.rikiandmorti.data.db.RikMortiEntity
import com.bagmanovam.rikiandmorti.data.model.RikMortiHeroDto
import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero


fun RikMortiHeroDto.toDomain(): RikMortiHero {
    return RikMortiHero(
        id = this.id,
        name = this.name,
        species = this.species,
        status = this.status,
        gender = this.gender,
        imageUrl = this.image
    )
}

fun RikMortiHeroDto.dtoToEntity(): RikMortiEntity {
    return RikMortiEntity(
        id = this.id,
        name = this.name,
        species = this.species,
        status = this.status,
        gender = this.gender,
        imageUrl = this.image
    )
}

fun RikMortiEntity.entityToDomain(): RikMortiHero {
    return RikMortiHero(
        id = this.id,
        name = this.name,
        species = this.species,
        status = this.status,
        gender = this.gender,
        imageUrl = this.imageUrl
    )
}

fun List<RikMortiHeroDto>.toDomains(): List<RikMortiHero> {
    return this.map { it.toDomain() }
}


fun List<RikMortiHero>.toEntities(): List<RikMortiEntity> {
    return this.map {
        RikMortiEntity(
            id = it.id,
            name = it.name,
            species = it.species,
            status = it.status,
            gender = it.gender,
            imageUrl = it.imageUrl
        )
    }
}

fun List<RikMortiEntity>.entitiesToDomains(): List<RikMortiHero> {
    return this.map {
        RikMortiHero(
            id = it.id,
            name = it.name,
            species = it.species,
            status = it.status,
            gender = it.gender,
            imageUrl = it.imageUrl
        )
    }
}