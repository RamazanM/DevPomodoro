package com.ramazanm.devpomodoro.data.repository

import com.ramazanm.devpomodoro.data.db.dao.PomodoroDAO
import com.ramazanm.devpomodoro.data.db.dao.TaskDAO
import com.ramazanm.devpomodoro.data.dto.PomodoroDTO
import com.ramazanm.devpomodoro.data.dto.TaskDTO
import com.ramazanm.devpomodoro.data.dto.TaskWithPomodorosDTO

class RoomTaskRepository(private val taskDAO: TaskDAO, private val pomodoroDAO: PomodoroDAO) :
    TaskRepository() {
    override suspend fun getTasksWithPomodoros(): List<TaskWithPomodorosDTO> {
        return taskDAO.getAllTasksWithPomodoros().map { it.toDTO() }
    }

    override suspend fun addPomodoro(
        pomodoroDTO: PomodoroDTO
    ) {
        pomodoroDAO.insert(pomodoroDTO.toEntity())
    }

    override suspend fun updatePomodoro(pomodoroDTO: PomodoroDTO) {
        pomodoroDAO.update(pomodoroDTO.toEntity())
    }

    override suspend fun getTask(id: Int): TaskDTO? {
        return taskDAO.getById(id)?.toDTO()
    }

    override suspend fun addTask(taskDTO: TaskDTO) {
        taskDAO.insert(taskDTO.toEntity())
    }

    override suspend fun updateTask(taskDTO: TaskDTO) {
        taskDAO.update(taskDTO.toEntity())
    }

    override suspend fun deleteTask(id: Int) {
        taskDAO.deleteById(id)
    }
}