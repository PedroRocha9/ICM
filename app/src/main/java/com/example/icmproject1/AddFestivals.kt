package com.example.icmproject1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.example.icmproject1.adapter.ArtistAdapter
import com.example.icmproject1.adapter.FestivalEntryAdapter
import com.example.icmproject1.data.Datasource

class AddFestivals : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_festivals)

        val festivalEntriesData = Datasource(this).loadFestivalEntries()

        val recyclerView = findViewById<RecyclerView>(R.id.festival_list)
        recyclerView.adapter = FestivalEntryAdapter(festivalEntriesData)
        recyclerView.setHasFixedSize(true)

        val choose = findViewById<Button>(R.id.choose)
        choose.setOnClickListener {
            goToActivity(Login::class.java)
        }
    }

    private val activityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // do something if returning to this activity
    }

    private fun goToActivity(activity: Class<*>) {
        val intent = Intent(this, activity)
        activityLauncher.launch(intent)
    }
}