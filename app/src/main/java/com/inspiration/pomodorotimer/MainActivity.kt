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
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener, Observer {
    lateinit var button : Button
    lateinit var textView : TextView
    lateinit var textView2 : TextView
    lateinit var time : PTimer

    private val vm : PViewModel by viewModels()

    class PTimer : Observable() {
        val posN = 25 * 60
        val negN = 5 * 60
        var value = posN
        fun minute() : String = (value / 60).toString() + "分" + (value % 60) + "秒"
        var flag : Boolean = true
        fun status() : String = if(flag) "仕事中" else "休憩中"
        lateinit var ob : Observer
        override fun addObserver(o: Observer?) {
            ob = o?:return
            super.addObserver(o)
        }
        fun count() {
            value--
            notifyObservers()
        }

        override fun notifyObservers() {
            ob.update(null, null)
        }

        fun reset() {
            flag = !flag
            value = if (flag) posN else negN
            notifyObservers()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("tag", "oncreate")


        time = PTimer()
        time.addObserver(this)

        button = findViewById(R.id.button)
        button.setOnClickListener(this)
        button.text = "スタート"
        textView = findViewById(R.id.textView)
        textView2 = findViewById(R.id.textView2)
        textView2.text = time.status()
        textView.text = time.minute()


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(p0: View?) {
        if (p0?.id == button.id) {
            button.visibility = Button.INVISIBLE
            val intent = Intent(this, PomodoroService::class.java)
            startService(intent)
            CoroutineScope(Dispatchers.Default).launch {
                while (time.value > 0) {
                    delay(1000)
                    time.count()
                    Log.d("tag", "coroutine")
                    if (time.value <= 0) {
                        ToneGenerator(AudioManager.STREAM_SYSTEM, ToneGenerator.MAX_VOLUME).also {
                            it.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT)
                        }
                        time.reset()
                    }
                }
            }
        }
    }

    override fun update(p0: Observable?, p1: Any?) {
        Log.d("tag", "update")
        CoroutineScope(Dispatchers.Main).launch {
            textView.text = time.minute()
            textView2.text = time.status()
        }
    }

}