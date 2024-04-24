package online.muhammadali.pomodoro.features.pomodoro.di

import android.content.Context

val contextProvider by lazy { ContextProvider() }

class ContextProvider internal constructor(){
    inline fun <T> runWithContext(block: Context.() -> T): T = block(ApplicationContextProvider.applicationContext)
}