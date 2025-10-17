package com.bagmanovam.rikiandmorti.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("rik_morti_items")
data class RikMortiEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val species: String,
    val status: String,
    val gender: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String
)