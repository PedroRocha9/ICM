package com.example.icmproject1

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.style.TtsSpan.ARG_DAY
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.icmproject1.adapter.ArtistAdapter
import com.example.icmproject1.data.Datasource
import com.example.icmproject1.model.Artist
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_DAY = "day"

/**
 * A simple [Fragment] subclass.
 * Use the [LineupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LineupFragment() : Fragment() {
    // TODO: Rename and change types of parameters
    private var nStages = 2
    private var day: Int = 1
    private var festivalUID : String = "Mso42n0MeMOZmKrKLfgQ"
    private val firestore = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            day = it.getInt(ARG_DAY)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_lineup, container, false)

        // get user's festival festivalUID from firestore
        val user = auth.currentUser
        if (user != null) {
            // get user chosenFestival
            val docRef = firestore.collection("users").document(user.uid).get().addOnSuccessListener {
                val festivalName = it.get("chosenFestival")
                // get festivalUID from festivalName
                val festivalRef = firestore.collection("festivals").whereEqualTo("name", festivalName).get().addOnSuccessListener { 
                    festivalUID = it.documents[0].id
                    Log.e(TAG, "festivalUID: $festivalUID")

                    // Load data for all stages
                    val coroutineScope = CoroutineScope(Dispatchers.Main)

                    coroutineScope.launch {
                        val day = Datasource(requireContext()).loadStages(day, festivalUID)
                        for (i in 0 until nStages) {
                            val stage = childFragmentManager.findFragmentByTag("stage$i") as StageFragment
                            val view = stage.rootView

                            val stageData = day[i]
                            view?.findViewById<TextView>(R.id.stage_name)?.text = stageData.stageName
                            val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view)
                            recyclerView?.adapter = ArtistAdapter(stageData.artists)
                            recyclerView?.setHasFixedSize(true)
                        }
                    }
                }
            }
        }

        // color the selected day
        val dayText = rootView.findViewById<TextView>(resources.getIdentifier("day$day", "id", requireContext().packageName))
        dayText?.setBackgroundColor(resources.getColor(R.color.background))

        val days = rootView.findViewById<LinearLayout>(R.id.days)
        if (days != null) {
            for (i in 0 until days.childCount) {
                val childView = days.getChildAt(i)
                if (childView is TextView) {
                    childView.setOnClickListener {
                        val dayText = childView.text.toString()
                        val dayNumber = dayText.replace("\\D+".toRegex(), "").toInt()
                        replaceFragment(R.id.lineup_wrapper, newInstance(dayNumber))
                    }
                }
            }
        }

        // Create and add multiple instances of the StageFragment to the container
        for (i in 0 until nStages) {
            val stage = StageFragment()
            addFragment(R.id.festivals_list, stage, "stage$i")
            addFragment(R.id.festivals_list, SeparatorFragment(), null)
        }

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    // Add fragment
    private fun addFragment(containerId: Int, fragment: Fragment, id: String? = null) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(containerId, fragment, id?: View.generateViewId().toString())
        transaction.commit()
    }

    // replace fragment function
    private fun replaceFragment(fragmentId : Int, fragment: Fragment) {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(fragmentId, fragment)
        fragmentTransaction.commit()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param day The day for the fragment.
         * @return A new instance of fragment LineupFragment.
         */
        @JvmStatic
        fun newInstance(day: Int) =
            LineupFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_DAY, day)
                }
            }
    }
}