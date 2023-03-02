package com.c1ph3rj.simplelogin.dbpkg

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Sentence::class], version = 1, exportSchema = false)
abstract class ListOfSentenceDb : RoomDatabase() {

    abstract fun ListOfSentenceDbDao():ListOfSentenceDbDao

    companion object{
        @Volatile
        private var INSTANCE: ListOfSentenceDb? = null

        fun getDb(context: Context):ListOfSentenceDb{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext
                ,ListOfSentenceDb::class.java, "list_of_sentence_table")
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}