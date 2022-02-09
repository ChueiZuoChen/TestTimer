package com.example.tester.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MyViewModel:ViewModel(){
    val time = mutableStateOf("")
    val startStopButton = mutableStateOf("Start")
}




