package dev.pimentel.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchQuery(
    @PrimaryKey @ColumnInfo(name = "query") val query: String
)
