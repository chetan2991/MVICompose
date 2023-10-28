package com.chetan.mvicompose.api

class AnimalRepo(private val animalApi: AnimalApi) {
    suspend fun getAnimals() = animalApi.getAnimals()
}