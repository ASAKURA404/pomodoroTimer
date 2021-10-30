package com.inspiration.pomodorotimer

import android.app.IntentService
import android.content.Intent
import android.util.Log
import android.widget.Toast

class PomodoroService : IntentService("hello") {

    override fun onHandleIntent(p0: Intent?) {
        Log.d("tag","intent")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("tag","intent")
        Toast.makeText(this, "strat", Toast.LENGTH_SHORT).show()
        return super.onStartCommand(intent, flags, startId)
    }

}