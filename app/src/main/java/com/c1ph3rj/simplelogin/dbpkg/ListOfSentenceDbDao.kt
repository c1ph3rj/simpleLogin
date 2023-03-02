package com.c1ph3rj.simplelogin.dbpkg

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ListOfSentenceDbDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun InsertSentence(sentence: Sentence)

    @Query("SELECT * FROM list_Of_sentence_table ORDER BY id ASC")
    fun readAllListOfSentence() : LiveData<List<Sentence>>
}