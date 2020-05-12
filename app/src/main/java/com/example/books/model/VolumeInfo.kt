package com.example.books.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VolumeInfo(
        @field:SerializedName("title") val title: String = "",
        @field:SerializedName("subtitle") val subTitle: String? = "",
        @field:SerializedName("authors") val authors: List<String>?,
        @field:SerializedName("publisher") val publisher: String? = "",
        @field:SerializedName("publishedDate") val publishedDate: String? = "",
        @field:SerializedName("description") val description: String? = "",
        @field:SerializedName("thumbnail") val thumbnail: String? = ""
) : Serializable