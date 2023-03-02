package com.c1ph3rj.simplelogin.dbpkg

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Sentence (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val sentence : String
)