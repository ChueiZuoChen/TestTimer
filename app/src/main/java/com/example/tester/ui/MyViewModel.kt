package com.example.tester.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt

class MyViewModel : ViewModel() {
    val timeText = mutableStateOf("")
    val startStopButton = mutableStateOf("Start")
    val timerStarted = mutableStateOf(false)
    val time = mutableStateOf(0.0)

    fun resetTimer() {
        stopTimer()
        time.value = 0.0
        timeText.value = getTimeStringFromDouble(time.value)
    }

    fun startStopTimer() {
        if (timerStarted.value)
            stopTimer()
        else
            startTimer()
    }

    fun startTimer() {
        startStopButton.value = "Stop"
        timerStarted.value = true
    }

    fun stopTimer() {
        startStopButton.value = "Start"
        timerStarted.value = false
    }

    val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time.value = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            timeText.value = getTimeStringFromDouble(time.value)
        }
    }

    fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }

    fun makeTimeString(hour: Int, min: Int, sec: Int): String =
        String.format("%02d:%02d:%02d", hour, min, sec)
}




