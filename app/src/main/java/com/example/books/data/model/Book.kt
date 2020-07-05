package com.example.books.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

@Entity(tableName = "books")
data class Book(
    @PrimaryKey @field:Json(name = "id") val id: String,
    @Embedded @field:Json(name = "volumeInfo") val volumeInfo: VolumeInfo
) : Serializable
