package com.chetan.mvicompose.api

import com.chetan.mvicompose.model.Animal
import retrofit2.http.GET

interface AnimalApi {
    @GET("animals.json")
    suspend fun getAnimals() : List<Animal>

}