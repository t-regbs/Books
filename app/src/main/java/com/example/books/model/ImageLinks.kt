package com.example.books.model

import com.squareup.moshi.Json
import java.io.Serializable

data class ImageLinks(
        @field:Json(name = "smallThumbnail") val smallThumbnail: String?,
        @field:Json(name = "thumbnail") val thumbnail: String?
) : Serializable