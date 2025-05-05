package com.mobile.gameofsecret.viewmodels

import android.app.Application
import com.mobile.gameofsecret.data.roomdb.getDatabase

class TaskViewModel(application: Application) : BaseViewModel(application) {
  private val db= getDatabase(application)

    private val taskDao =db.taskDao()

    fun getTaskList(){

    }

    fun resetTask(taskList: List<String>,onComplete:()-> Unit){

    }

}