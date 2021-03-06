package dev.pimentel.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class CategoryDTO(
    @PrimaryKey @ColumnInfo(name = "name") val name: String
)
