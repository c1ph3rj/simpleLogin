package com.c1ph3rj.simplelogin.dbpkg

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Sentence (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    @ColumnInfo
    val sentence : String
)