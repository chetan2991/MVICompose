package com.chetan.mvicompose.view

import com.chetan.mvicompose.model.Animal

sealed class MainState{
    object Idel : MainState()
    object Loading : MainState()
    data class Animals(val animals : List<Animal>) : MainState()
    data class Error(val error : String? ): MainState()
}
