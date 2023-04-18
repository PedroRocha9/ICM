package com.example.icmproject1.data

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.icmproject1.R
import com.example.icmproject1.model.Artist
import com.example.icmproject1.model.FestivalEntry
import com.example.icmproject1.model.Stage

class Datasource(private val context: Context) {
    private val resources = context.resources

    fun loadStages(day : Int): List<Stage> {
        when (day) {
            1 -> {
                return listOf(Stage(
                    resources.getStringArray(R.array.stages)[0],
                    arrayOf(
                        Artist(resources.getStringArray(R.array.artists)[0], "19:00"),
                        Artist(resources.getStringArray(R.array.artists)[2], "21:00"),
                        Artist(resources.getStringArray(R.array.artists)[4], "23:00"),
                        Artist(resources.getStringArray(R.array.artists)[5], "00:00"),
                        Artist(resources.getStringArray(R.array.artists)[8], "02:00"),
                    )), Stage(
                    resources.getStringArray(R.array.stages)[1],
                    arrayOf(
                        Artist(resources.getStringArray(R.array.artists)[1], "22:00"),
                        Artist(resources.getStringArray(R.array.artists)[3], "00:00")
                    ))
                )
            }
            2 -> {
                return listOf(Stage(
                    resources.getStringArray(R.array.stages)[0],
                    arrayOf(
                        Artist(resources.getStringArray(R.array.artists)[0], "19:00"),
                        Artist(resources.getStringArray(R.array.artists)[3], "23:00"),
                        Artist(resources.getStringArray(R.array.artists)[2], "00:00"),
                        Artist(resources.getStringArray(R.array.artists)[1], "01:00"),
                    )), Stage(
                    resources.getStringArray(R.array.stages)[1],
                    arrayOf(
                        Artist(resources.getStringArray(R.array.artists)[8], "21:00"),
                        Artist(resources.getStringArray(R.array.artists)[4], "22:00"),
                        Artist(resources.getStringArray(R.array.artists)[7], "00:00"),
                        Artist(resources.getStringArray(R.array.artists)[6], "02:00"),
                        Artist(resources.getStringArray(R.array.artists)[5], "02:00")
                    ))
                )
            }
            3 -> {
                return listOf(Stage(
                    resources.getStringArray(R.array.stages)[0],
                    arrayOf(
                        Artist(resources.getStringArray(R.array.artists)[8], "19:00"),
                        Artist(resources.getStringArray(R.array.artists)[6], "21:00"),
                    )), Stage(
                    resources.getStringArray(R.array.stages)[1],
                    arrayOf(
                        Artist(resources.getStringArray(R.array.artists)[1], "22:00"),
                        Artist(resources.getStringArray(R.array.artists)[3], "00:00"),
                        Artist(resources.getStringArray(R.array.artists)[5], "01:00"),
                        Artist(resources.getStringArray(R.array.artists)[4], "03:00")
                    ))
                )
            }
            4 -> {
                return listOf(Stage(
                    resources.getStringArray(R.array.stages)[0],
                    arrayOf(
                        Artist(resources.getStringArray(R.array.artists)[3], "17:00"),
                        Artist(resources.getStringArray(R.array.artists)[8], "19:00"),
                        Artist(resources.getStringArray(R.array.artists)[7], "21:00"),
                        Artist(resources.getStringArray(R.array.artists)[2], "22:00"),
                    )), Stage(
                    resources.getStringArray(R.array.stages)[1],
                    arrayOf(
                        Artist(resources.getStringArray(R.array.artists)[6], "18:00"),
                        Artist(resources.getStringArray(R.array.artists)[5], "20:00"),
                        Artist(resources.getStringArray(R.array.artists)[7], "00:00"),
                    ))
                )
            }
        }
        return listOf()
    }

    fun loadFestivalEntries(): List<FestivalEntry> {
        val festivalNames = resources.getStringArray(R.array.festivals)
        val festivalLocations = resources.getStringArray(R.array.locations)
        return festivalNames.mapIndexed { index, name ->
            FestivalEntry(name, festivalLocations[index])
        }
    }
}