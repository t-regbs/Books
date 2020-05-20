package com.example.books.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ImageLinks(
        @field:SerializedName("smallThumbnail") val smallThumbnail: String?,
        @field:SerializedName("thumbnail") val thumbnail: String?
) : Serializable