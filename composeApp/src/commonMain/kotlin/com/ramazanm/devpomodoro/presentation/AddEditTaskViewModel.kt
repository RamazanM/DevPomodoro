package com.ramazanm.devpomodoro.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.ramazanm.devpomodoro.Routes
import com.ramazanm.devpomodoro.data.dto.TaskDTO
import com.ramazanm.devpomodoro.data.dto.taskDTONavTypeMapper
import com.ramazanm.devpomodoro.data.repository.TaskRepository

abstract class AddEditTaskViewModel() : ViewModel() {
    abstract val task: MutableState<TaskDTO>
    abstract val isEditing: MutableState<Boolean>

    abstract suspend fun saveTask()
    abstract suspend fun deleteTask()
}

class AddEditTaskViewModelImpl(
    savedStateHandle: SavedStateHandle, val repository: TaskRepository
) : AddEditTaskViewModel() {
    val taskFromRoute: TaskDTO? = savedStateHandle.toRoute<Routes.AddEditTaskScreen>(typeMap = taskDTONavTypeMapper).taskDTO
    override val task = if(taskFromRoute!=null) mutableStateOf(taskFromRoute) else mutableStateOf(
        TaskDTO()
    )
    override val isEditing: MutableState<Boolean> = mutableStateOf(taskFromRoute != null)

    override suspend fun saveTask() {
        task.value.let { repository.addTask(it) }
    }

    override suspend fun deleteTask() {
        task.value.id?.let { repository.deleteTask(it) }
    }
}

class TestAddEditTaskViewModelImpl() : AddEditTaskViewModel() {
    override val task: MutableState<TaskDTO> = mutableStateOf(TaskDTO())
    override val isEditing: MutableState<Boolean> = mutableStateOf(false)


    override suspend fun saveTask() {
    }

    override suspend fun deleteTask() {
    }

}