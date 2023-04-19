package com.example.icmproject1.data

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.example.icmproject1.R
import com.example.icmproject1.model.*
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

        val mainStageMap = dayMap["mainStage"] as? Map<String, List<String>>
        val secondaryStageMap = dayMap["secStage"] as? Map<String, List<String>>

        if (mainStageMap != null) {
            val mainStageArtists = mainStageMap["artists"] ?: emptyList()
            val mainStageHours = mainStageMap["hours"] ?: emptyList()
            val mainStage = mainStageArtists.zip(mainStageHours).map { Artist(it.first, it.second) }.toTypedArray()

            stagesList.add(Stage("Main Stage", mainStage))
        }

        if (secondaryStageMap != null) {
            val secondaryStageArtists = secondaryStageMap["artists"] ?: emptyList()
            val secondaryStageHours = secondaryStageMap["hours"] ?: emptyList()
            val secondaryStage = secondaryStageArtists.zip(secondaryStageHours).map { Artist(it.first, it.second) }.toTypedArray()

            stagesList.add(Stage("Secondary Stage", secondaryStage))
        }

        return stagesList
    }

    suspend fun loadFestival(festivalUID: String): FestivalEntry {
        val festivalDoc = firestore.collection("festivals").document(festivalUID).get().await()
        val name = festivalDoc.getString("name")!!
        val location = festivalDoc.getString("location")!!
        val coordinates = festivalDoc.getGeoPoint("coords")!!
        Log.e("FESTIVALS", name)
        return FestivalEntry(name, location, Coordinates(coordinates.latitude, coordinates.longitude))
    }

    suspend fun loadFestivalEntries(): List<FestivalEntry> {
        val festivalEntriesList = mutableListOf<FestivalEntry>()
        val festivalsSnapshot = firestore.collection("festivals").get().await()

        Log.e("FESTIVALS", "HEREEEEEE")

        for (festivalDoc in festivalsSnapshot) {
            val name = festivalDoc.getString("name")!!
            Log.e("FESTIVALS", name)
            val location = festivalDoc.getString("location")!!
            val coordinates = festivalDoc.getGeoPoint("coords")!!
            festivalEntriesList.add(FestivalEntry(name, location, Coordinates(coordinates.latitude, coordinates.longitude)))
        }

        Log.e("FESTIVALS", festivalEntriesList.toString())

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
            val coordinates = festivalDoc.getGeoPoint("coords")!!
            userFestivalsList.add(FestivalEntry(name, location, Coordinates(coordinates.latitude, coordinates.longitude)))
        }

        return userFestivalsList
    }

    suspend fun getUserLocation(userUID : String) : Coordinates {
        val userDoc = firestore.collection("users").document(userUID).get().await()
        val coordinates = userDoc.getGeoPoint("coords")!!
        val user = User(userUID, Coordinates(coordinates.latitude, coordinates.longitude))
        return user.coordinates
    }

    suspend fun getUserBuddies(username: String): List<String> {
        val userDoc = firestore.collection("users").document(username).get().await()
        val userBuddies = userDoc.get("buddies") as List<String>

        return userBuddies
    }
}
