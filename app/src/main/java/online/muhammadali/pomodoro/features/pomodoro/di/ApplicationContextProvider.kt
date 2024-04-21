package online.muhammadali.pomodoro.features.pomodoro.di

import android.app.Application
import android.content.Context

object ApplicationContextProvider {
    lateinit var applicationContext: Context
        private set

    fun Application.updateContext(): Context = applicationContext
}