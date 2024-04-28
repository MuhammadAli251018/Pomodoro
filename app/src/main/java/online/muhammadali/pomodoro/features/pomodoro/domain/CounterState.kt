package online.muhammadali.pomodoro.features.pomodoro.domain

import kotlin.time.Duration

interface CounterState {
    val session: Duration
    val onGoing: Boolean
}