package com.example.icmproject1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.example.icmproject1.adapter.FestivalEntryAdapter
import com.example.icmproject1.adapter.OnItemClickListener
import com.example.icmproject1.data.Datasource
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class AddFestivals : AppCompatActivity(), OnItemClickListener {
    private lateinit var chosenFestival : String
    private lateinit var chosenLocation : String
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_festivals)

        val festivalEntriesData = Datasource(this).loadFestivalEntries()

        val recyclerView = findViewById<RecyclerView>(R.id.festival_list)
        recyclerView.adapter = FestivalEntryAdapter(festivalEntriesData, this)
        recyclerView.setHasFixedSize(true)

        val choose = findViewById<Button>(R.id.choose)
        choose.setOnClickListener {
            if (choose.isEnabled){
                goToActivity(Lineup::class.java)
                db.collection("users").document("user").update("festival", chosenFestival)
                finish()
            }
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

    override fun onItemClick(view : View, position: Int) {
        // activate choose button
        val choose = findViewById<Button>(R.id.choose)
        if (!choose.isEnabled) {
            choose.isEnabled = true
            choose.setBackgroundColor(resources.getColor(R.color.main))
        }

        // select the clicked one
        view.background = getDrawable(R.drawable.selected)
        view.findViewById<TextView>(R.id.festival_title).setTextColor(resources.getColor(R.color.background))
        view.findViewById<TextView>(R.id.festival_location).setTextColor(resources.getColor(R.color.background))

        view.findViewById<TextView>(R.id.festival_title).text.let {
            chosenFestival = it.toString()
            Log.d("AddFestivals", "Chosen festival is $chosenFestival")
        }

        view.findViewById<TextView>(R.id.festival_location).text.let {
            chosenLocation = it.toString()
            Log.d("AddFestivals", "Chosen location is $chosenLocation")
        }

        // deselect all others
        val recyclerView = view.parent as RecyclerView
        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(i)
            if (child != view) {
                child.setBackgroundResource(android.R.color.transparent)
                child.findViewById<TextView>(R.id.festival_title).setTextColor(resources.getColor(R.color.text))
                child.findViewById<TextView>(R.id.festival_location).setTextColor(resources.getColor(R.color.text))
            }
        }
    }
}