package com.ramazanm.devpomodoro.di

import androidx.lifecycle.SavedStateHandle
import com.ramazanm.devpomodoro.data.db.getPomodoroDAO
import com.ramazanm.devpomodoro.data.db.getRoomDatabase
import com.ramazanm.devpomodoro.data.db.getTaskDAO
import com.ramazanm.devpomodoro.data.repository.RoomTaskRepository
import com.ramazanm.devpomodoro.data.repository.TaskRepository
import com.ramazanm.devpomodoro.presentation.AddEditTaskViewModel
import com.ramazanm.devpomodoro.presentation.AddEditTaskViewModelImpl
import com.ramazanm.devpomodoro.presentation.TaskListViewModel
import com.ramazanm.devpomodoro.presentation.TaskListViewModelImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

expect fun platformModule(): Module
val commonModule = module {
    single { getRoomDatabase(get()) }
    single { getTaskDAO(get()) }
    single { getPomodoroDAO(get()) }

    single { RoomTaskRepository(get(), get()) }.bind(TaskRepository::class)
    viewModel { TaskListViewModelImpl(get()) }.bind(TaskListViewModel::class)
    viewModel { (handle: SavedStateHandle)-> AddEditTaskViewModelImpl(handle,get()) }.bind(AddEditTaskViewModel::class)
}