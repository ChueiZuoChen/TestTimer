package com.example.tester

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.example.tester.ui.MyViewModel
import com.example.tester.ui.TimerService

class MainActivity : ComponentActivity() {
    lateinit var serviceIntent: Intent
    lateinit var viewModel: MyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = MyViewModel()
        serviceIntent =
            Intent(application.applicationContext, TimerService::class.java)
        registerReceiver(viewModel.updateTime, IntentFilter(TimerService.TIMER_UPDATED))
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
                    Text(text = viewModel.timeText.value, modifier = Modifier.layoutId("timer"))
                    Button(
                        onClick = {
                            viewModel.startStopTimer()
                            if (viewModel.timerStarted.value) {
                                serviceIntent.putExtra(
                                    TimerService.TIME_EXTRA,
                                    viewModel.time.value
                                )
                                startService(serviceIntent)
                            } else {
                                stopService(serviceIntent)
                            }
                        },
                        modifier = Modifier.layoutId("startstop")
                    ) {
                        Text(text = viewModel.startStopButton.value)
                    }
                    Button(
                        onClick = {
                            viewModel.resetTimer()
                            stopService(serviceIntent)
                        },
                        modifier = Modifier.layoutId("reset")
                    ) {
                        Text(text = "RESET")
                    }

                }
            }
        }
    }
}




