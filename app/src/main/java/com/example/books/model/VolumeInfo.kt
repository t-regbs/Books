package com.example.books.model

import androidx.room.Embedded
import com.squareup.moshi.Json
import java.io.Serializable

data class VolumeInfo(
        @field:Json(name = "title") val title: String = "",
        @field:Json(name = "subtitle") val subTitle: String? = "",
        @field:Json(name = "authors") val authors: List<String>?,
        @field:Json(name = "publisher") val publisher: String? = "",
        @field:Json(name = "publishedDate") val publishedDate: String? = "",
        @field:Json(name = "description") val description: String? = "",
        @Embedded @field:Json(name = "imageLinks") val imageLinks: ImageLinks
) : Serializable