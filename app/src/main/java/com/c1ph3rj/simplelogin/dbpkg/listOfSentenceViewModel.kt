package com.c1ph3rj.simplelogin.dbpkg

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class listOfSentenceViewModel(application: Application) : AndroidViewModel(application) {

    private var readListOfSentence: LiveData<List<Sentence>>
    private var repository: ListOfSentenceDbRepository

    init {
        val listOfSentenceDbDao = ListOfSentenceDb.getDb(application.applicationContext).ListOfSentenceDbDao()
        repository = ListOfSentenceDbRepository(listOfSentenceDbDao)
        readListOfSentence = repository.readAllSentence
    }

    fun addSentence(sentence: Sentence){
        viewModelScope.launch (Dispatchers.IO) {
            repository.addSentence(sentence)
        }
    }
}