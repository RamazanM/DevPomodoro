package com.ramazanm.devpomodoro

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.ramazanm.devpomodoro.di.initKoin
import org.koin.core.context.startKoin

fun main() {
    initKoin()

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "DevPomodoro",
        ) {
            App()
        }
    }
}