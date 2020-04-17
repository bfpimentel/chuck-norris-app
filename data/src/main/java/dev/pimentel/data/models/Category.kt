package dev.pimentel.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey @ColumnInfo(name = "name") val name: String
)
