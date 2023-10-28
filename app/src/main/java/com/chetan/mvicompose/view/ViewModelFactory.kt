package com.chetan.mvicompose.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.chetan.mvicompose.api.AnimalApi
import com.chetan.mvicompose.api.AnimalRepo

class ViewModelFactory(private val api : AnimalApi ) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       if(modelClass.isAssignableFrom(MainViewModel::class.java)){
           return MainViewModel(AnimalRepo(api)) as T
       }
        throw IllegalArgumentException("Unkown Class Name")
    }
}