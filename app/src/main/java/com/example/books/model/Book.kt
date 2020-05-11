package com.example.books.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "books")
data class Book(
        @PrimaryKey @field:SerializedName("id") val id: String,
        @field:SerializedName("title") val title: String,
        @field:SerializedName("subtitle") val subTitle: String,
        @field:SerializedName("authors") val authors: String,
        @field:SerializedName("publisher") val publisher: String,
        @field:SerializedName("publishedDate") val publishedDate: String,
        @field:SerializedName("description") val description: String,
        @field:SerializedName("thumbnail") val thumbnail: String
)
