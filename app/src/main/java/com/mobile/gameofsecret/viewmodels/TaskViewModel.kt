package com.mobile.gameofsecret.viewmodels

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.mobile.gameofsecret.data.model.Gamer
import com.mobile.gameofsecret.data.model.Task
import com.mobile.gameofsecret.data.roomdb.getDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskViewModel(application: Application) : BaseViewModel(application) {
    private val db = getDatabase(application)

    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    val taskList: StateFlow<List<Task>> = _taskList.asStateFlow()
    private val taskDao = db.taskDao()

    fun getTaskList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                 val list=taskDao.getTaskAndId()
                withContext(Dispatchers.Main){
                    _taskList.value=list
                }
            } catch (e: Exception) {
                e.printStackTrace()
                handleException(e)
            }
        }
    }

    fun resetTask(taskFields: List<String>, onComplete: () -> Unit) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                taskDao.deleteAll()

                taskFields.forEach { task ->
                    taskDao.insert(Task(task))
                }


                _taskList.value = taskDao.getTaskAndId()

                withContext(Dispatchers.Main) {
                    onComplete()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                handleException(e)
            }
        }
    }

}