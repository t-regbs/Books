package com.example.books.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "books")
data class Book(
        @PrimaryKey @field:SerializedName("id") val id: String,
        @Embedded @field:SerializedName("volumeInfo") val volumeInfo: VolumeInfo
) : Serializable
