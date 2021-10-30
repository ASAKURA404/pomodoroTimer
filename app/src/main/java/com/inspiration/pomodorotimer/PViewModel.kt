package com.inspiration.pomodorotimer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class PViewModel : ViewModel() {
    private var time : LiveData<Int>
}