package com.inspiration.pomodorotimer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class PViewModel : ViewModel() {
    var status = MutableLiveData<String>()
    var displayTime = MutableLiveData<String>()
    var time = PTimer(10,5)

    fun count() {
        time.count()
        update()
    }

    fun reset() {
        time.reset()
        update()
    }

    fun update() {
        status.value = time.status()
        displayTime.value = time.minute()
    }

}