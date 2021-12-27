package com.inspiration.pomodorotimer

import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import java.util.*
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity(){

    private val pViewModel : PViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("tag", "oncreate")
        super.onCreate(savedInstanceState)

        setContent {
            ConversationScreen(pViewModel)
        }
    }

    @Composable
    fun ConversationScreen(vm:PViewModel) {
        val status : String by  vm.status.observeAsState("状態")
        val displayTime : String by vm.displayTime.observeAsState("時間")
        TimerView(status, displayTime)
    }

    @Composable
    fun TimerView(status : String, displayTime : String) {
        var isRunning by remember { mutableStateOf(false)}
        var isVisible by remember { mutableStateOf(true)}
        val padding = 4.dp
        val textSize = 50.sp
        Column (
            Modifier
                .padding(padding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
                ) {
            Text(
                text = status,
                color = Color.Red,
                fontSize = textSize,
                textAlign = TextAlign.Center
            )
            Text(
                text = displayTime,
                color = Color.Red,
                fontSize = textSize,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1F, true))
            if (isVisible) {
                Button(onClick = { isRunning = !isRunning; isVisible = isVisible }) {
                    Text(text = "hello",
                    fontSize = textSize * 2)
                }
            }
        }

        LaunchedEffect(key1 = isRunning, block = {
            if (isRunning) {
                timerStart()
            }
        })
    }

    @Preview
    @Composable
    fun previewTextView() {
        TimerView("status", "displayTime")
    }

    private suspend fun timerStart(){
        while (true) {
            if (pViewModel.time.nowValue > 0) {
                Log.d("tag", pViewModel.time.nowValue.toString())
                CoroutineScope(Dispatchers.Main).async {
                    delay(1000)
                    pViewModel.count()
                }.await()
            }
            else {
                beep()
                CoroutineScope(Dispatchers.Main).async {
                    pViewModel.reset()
                }.await()
            }
        }
    }

    private fun beep() {
        ToneGenerator(AudioManager.STREAM_SYSTEM, ToneGenerator.MAX_VOLUME).also {
            it.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT)
        }
    }
}