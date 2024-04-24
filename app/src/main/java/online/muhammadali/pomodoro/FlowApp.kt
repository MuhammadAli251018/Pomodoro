package online.muhammadali.pomodoro

import android.app.Application
import online.muhammadali.pomodoro.features.pomodoro.di.ApplicationContextProvider
import online.muhammadali.pomodoro.features.pomodoro.di.ApplicationContextProvider.updateContext

class FlowApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ApplicationContextProvider.apply {
            updateContext()
        }
    }
}