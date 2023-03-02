package com.c1ph3rj.simplelogin.dbpkg

class ListOfSentenceDbRepository(private val listOfSentenceDbDao: ListOfSentenceDbDao) {

    val readAllSentence = listOfSentenceDbDao.readAllListOfSentence()

    suspend fun addSentence(sentence: Sentence){
        listOfSentenceDbDao.InsertSentence(sentence)
    }

}