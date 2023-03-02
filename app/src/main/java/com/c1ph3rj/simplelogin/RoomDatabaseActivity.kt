package com.c1ph3rj.simplelogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import com.c1ph3rj.simplelogin.databinding.ActivityRoomDatabaseBinding
import com.google.android.material.textfield.TextInputEditText

class RoomDatabaseActivity : AppCompatActivity() {
    lateinit var addField: TextInputEditText
    lateinit var addFieldBtn: Button
    lateinit var listOfFieldsView: ListView
    lateinit var bindRoomDbView: ActivityRoomDatabaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindRoomDbView = ActivityRoomDatabaseBinding.inflate(layoutInflater)
        setContentView(bindRoomDbView.root)


        try{
            init()
        }catch(e: Exception){
            e.printStackTrace()
        }
    }

    fun init(){
        try{
            addField = bindRoomDbView.AddField
            addFieldBtn = bindRoomDbView.AddFieldBtn
            listOfFieldsView = bindRoomDbView.listOfDataView



        }catch(e: Exception){
            e.printStackTrace()
        }
    }
}