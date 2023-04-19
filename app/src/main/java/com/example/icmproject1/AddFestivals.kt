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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text

class AddFestivals : AppCompatActivity(), OnItemClickListener {
    private lateinit var chosenFestival : String
    private lateinit var chosenLocation : String
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_festivals)

        val recyclerView = findViewById<RecyclerView>(R.id.festival_list)
        recyclerView.setHasFixedSize(true)

        val choose = findViewById<Button>(R.id.choose)
        choose.isEnabled = false

        val coroutineScope = CoroutineScope(Dispatchers.Main)

        Log.e("AddFestivals", "onCreate:")

        coroutineScope.launch {
            Log.e("AddFestivals", "launch:")
            val festivalEntriesData = withContext(Dispatchers.IO) {
                Datasource(this@AddFestivals).loadFestivalEntries()
            }
            recyclerView.adapter = FestivalEntryAdapter(festivalEntriesData, this@AddFestivals)
            choose.isEnabled = true
        }

        choose.setOnClickListener {
            if (choose.isEnabled) {
                val currentUser = Firebase.auth.currentUser
                if (currentUser != null) {
                    val uid = currentUser.uid
                    Log.e("AddFestivals", "uid: $uid")

                    db.collection("users").document(uid).update("chosenFestival", chosenFestival, "festivalLocation", chosenLocation)
                        .addOnSuccessListener {
                            Log.d("AddFestivals", "User document updated")
                        }
                        .addOnFailureListener { e ->
                            Log.w("AddFestivals", "Error updating user document", e)
                        }
                }
                goToActivity(Lineup::class.java)
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