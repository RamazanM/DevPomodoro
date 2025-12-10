package com.ramazanm.devpomodoro.data

import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object Preferences {
    val POMODORO_DURATION = "POMODORO_DURATION" to 25.minutes
    val SHORT_BREAK_DURATION = "SHORT_BREAK_DURATION" to 5.minutes
    val LONG_BREAK_DURATION = "LONG_BREAK_DURATION" to 15.minutes


}