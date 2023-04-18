package com.example.icmproject1.model
data class FestivalEntry(val name: String, val location: String, val coordinates: Coordinates)

data class Coordinates(val latitude : Double, val longitude : Double)
