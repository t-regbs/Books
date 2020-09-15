package com.example.books.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val bookId: String,
    val prevKey: Int?,
    val nextKey: Int?
)