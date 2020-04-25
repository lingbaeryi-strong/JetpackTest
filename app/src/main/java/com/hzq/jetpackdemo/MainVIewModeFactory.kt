package com.hzq.jetpackdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainVIewModeFactory(private var countResvered: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(countResvered) as T
    }

}