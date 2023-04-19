package com.example.icmproject1.data

import android.content.Context
import com.example.icmproject1.R
import com.example.icmproject1.model.Artist
import com.example.icmproject1.model.Coordinates
import com.example.icmproject1.model.FestivalEntry
import com.example.icmproject1.model.Stage
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class Datasource(private val context: Context) {
    private val resources = context.resources
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun loadStages(day: Int, festivalUID: String): List<Stage> {
        val stagesList = mutableListOf<Stage>()
        val festivalDoc = firestore.collection("festivals").document(festivalUID).get().await()
        val days = festivalDoc.get("days") as List<Map<String, Any>>
        val dayMap = days[day - 1]

        for ((stageName, artistsData) in dayMap) {
            val artists = (artistsData as List<Map<String, String>>).map { Artist(it["name"]!!, it["hour"]!!) }.toTypedArray()
            stagesList.add(Stage(stageName, artists))
        }

        return stagesList
    }

    suspend fun loadFestivalEntries(): List<FestivalEntry> {
        val festivalEntriesList = mutableListOf<FestivalEntry>()
        val festivalsSnapshot = firestore.collection("festivals").get().await()

        for (festivalDoc in festivalsSnapshot) {
            val name = festivalDoc.getString("name")!!
            val location = festivalDoc.getString("location")!!
            val coordinates = festivalDoc.getGeoPoint("coordinates")!!
            festivalEntriesList.add(FestivalEntry(name, location, Coordinates(coordinates.latitude, coordinates.longitude)))
        }

        return festivalEntriesList
    }

    suspend fun getUserFestivals(username: String): List<FestivalEntry> {
        val userFestivalsList = mutableListOf<FestivalEntry>()
        val userDoc = firestore.collection("users").document(username).get().await()
        val userFestivals = userDoc.get("festivals") as List<String>

        for (festivalUID in userFestivals) {
            val festivalDoc = firestore.collection("festivals").document(festivalUID).get().await()
            val name = festivalDoc.getString("name")!!
            val location = festivalDoc.getString("location")!!
            val coordinates = festivalDoc.getGeoPoint("coordinates")!!
            userFestivalsList.add(FestivalEntry(name, location, Coordinates(coordinates.latitude, coordinates.longitude)))
        }

        return userFestivalsList
    }

    suspend fun getUserLocation(username: String): Coordinates {
        val userDoc = firestore.collection("users").document(username).get().await()
        val coordinates = userDoc.getGeoPoint("location")!!

        return Coordinates(coordinates.latitude, coordinates.longitude)
    }

    suspend fun getUserBuddies(username: String): List<String> {
        val userDoc = firestore.collection("users").document(username).get().await()
        val userBuddies = userDoc.get("buddies") as List<String>

        return userBuddies
    }
}
