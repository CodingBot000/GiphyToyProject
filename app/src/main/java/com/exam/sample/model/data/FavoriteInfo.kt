package com.exam.sample.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TABLE_FAVORITE")
class FavoriteInfo(
    @PrimaryKey @ColumnInfo(name = "userId") var userId: String,
    @ColumnInfo(name = "gifUrl") var gifUrl: String,
    @ColumnInfo(name = "type") var type: String
)
