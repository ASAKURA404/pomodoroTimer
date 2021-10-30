package com.inspiration.pomodorotimer

import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import java.util.*
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity(), View.OnClickListener {
    lateinit var button : Button
    lateinit var textTime : TextView
    lateinit var textStatus : TextView

    private val pViewModel : PViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var a = "hello hello"
        findViewById<ComposeView>(R.id.view).apply {
            setContent {
                TimerView(pViewModel)
            }
        }
        Log.d("tag", "oncreate")

        button = findViewById(R.id.button)
        button.setOnClickListener(this)
        button.text = "スタート"
        textTime = findViewById(R.id.textTime)
        textStatus = findViewById(R.id.textStatus)

        pViewModel.status.observe(this) { textStatus.text = it }
        pViewModel.displayTime.observe(this) { textTime.text = it }

    }

    @Composable
    fun TimerView(vm : PViewModel) {
        val status : String by vm.status.observeAsState("")
        val displayTime : String by vm.displayTime.observeAsState("")
        Column {
            Text(text = status,
            color = Color.Red,
            fontSize = 30.sp
            )
            Text(text = displayTime,
            color = Color.Red,
            fontSize = 30.sp)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(p0: View?) {
        if (p0?.id == button.id) {
            Log.d("tag", "push")
            button.visibility = Button.INVISIBLE
            val intent = Intent(this, PomodoroService::class.java)
            startService(intent)
            CoroutineScope(Dispatchers.Default).launch {
                timerStart()
            }
        }
    }

    private suspend fun timerStart(){
        while (true) {
            if (pViewModel.time.nowValue > 0) {
                delay(1000)
                Log.d("tag", pViewModel.time.nowValue.toString())
                CoroutineScope(Dispatchers.Main).async {
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