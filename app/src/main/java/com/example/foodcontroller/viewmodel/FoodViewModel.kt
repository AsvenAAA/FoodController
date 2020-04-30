package com.example.foodcontroller.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.foodcontroller.repository.local.TempData

class FoodViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TempData
    val currentEatenFood: LiveData<Array<String>>

    init {
        repository = TempData()
        currentEatenFood = repository.myData
    }
}