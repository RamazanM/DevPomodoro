package com.ramazanm.devpomodoro.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramazanm.devpomodoro.data.dto.TaskWithPomodorosDTO
import com.ramazanm.devpomodoro.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class TaskListState {
    object Loading : TaskListState()
    data class Success(val tasks: List<TaskWithPomodorosDTO>) : TaskListState()
    data class Error(val message: String) : TaskListState()
    object Empty : TaskListState()
}

abstract class TaskListViewModel(): ViewModel(){
    abstract val taskListState: StateFlow<TaskListState>
    abstract fun loadTasks()
}

class TaskListViewModelImpl(val repository: TaskRepository) : TaskListViewModel() {
    private val _taskListState = MutableStateFlow<TaskListState>(TaskListState.Loading)
    override val taskListState: StateFlow<TaskListState> = _taskListState

    init {
        loadTasks()
    }

    override fun loadTasks() {
        viewModelScope.launch {
            _taskListState.update { TaskListState.Loading }
            try {
                val tasks = repository.getTasksWithPomodoros()
                if (tasks.isEmpty()) {
                    _taskListState.update { TaskListState.Empty }
                } else {
                    _taskListState.update {
                        TaskListState.Success(tasks)
                    }
                }
            } catch (e: Exception) {
                _taskListState.update {
                    TaskListState.Error(e.message ?: "Unknown error")
                }
            }

        }

    }


}