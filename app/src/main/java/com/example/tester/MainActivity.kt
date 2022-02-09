package com.example.tester

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.example.tester.ui.Constains
import com.example.tester.ui.MyViewModel
import com.example.tester.ui.TimerService
import com.example.tester.ui.theme.TesterTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    lateinit var serviceIntent: Intent
    lateinit var viewModel: MyViewModel
    var time = 0.0
    var timerStarted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = MyViewModel()
        serviceIntent =
            Intent(application.applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(Constains.TIMER_UPDATED))
        setContent {
            fun updateConstraints(): ConstraintSet {
                return ConstraintSet {
                    val timer = createRefFor("timer")
                    val startstop = createRefFor("startstop")
                    val reset = createRefFor("reset")
                    constrain(timer) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    constrain(startstop) {
                        top.linkTo(parent.top, margin = 30.dp)
                        start.linkTo(parent.start, margin = 30.dp)
                    }
                    constrain(reset) {
                        top.linkTo(parent.top, margin = 30.dp)
                        end.linkTo(parent.end, margin = 30.dp)
                    }
                }
            }
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                ConstraintLayout(updateConstraints(), modifier = Modifier.fillMaxSize()) {
                    Text(text = viewModel.time.value, modifier = Modifier.layoutId("timer"))
                    Button(
                        onClick = { startStopTimer() },
                        modifier = Modifier.layoutId("startstop")
                    ) {
                        Text(text = viewModel.startStopButton.value)
                    }
                    Button(onClick = { resetTimer() }, modifier = Modifier.layoutId("reset")) {
                        Text(text = "RESET")
                    }

                }
            }
        }
    }

    private fun resetTimer() {
        stopTimer()
        time = 0.0
        viewModel.time.value = getTimeStringFromDouble(time)
    }

    private fun startStopTimer() {
        if (timerStarted)
            stopTimer()
        else
            startTimer()
    }

    private fun startTimer() {
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        startService(serviceIntent)
        viewModel.startStopButton.value = "Stop"
        timerStarted = true
    }

    private fun stopTimer() {
        stopService(serviceIntent)
        viewModel.startStopButton.value = "Start"
        timerStarted = false
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            viewModel.time.value = getTimeStringFromDouble(time)
        }
    }

    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hour: Int, min: Int, sec: Int): String =
        String.format("%02d:%02d:%02d", hour, min, sec)
}




