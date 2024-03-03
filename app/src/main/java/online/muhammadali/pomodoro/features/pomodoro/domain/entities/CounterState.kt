package online.muhammadali.pomodoro.features.pomodoro.domain.entities

import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration

interface CounterState {
    val session: Duration
    val onGoing: Boolean
}