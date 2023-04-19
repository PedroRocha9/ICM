package com.example.icmproject1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.example.icmproject1.adapter.FestivalEntryAdapter
import com.example.icmproject1.adapter.StringListAdapter
import com.example.icmproject1.data.Datasource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var nickname: String = "powerlol"
    private var email: String = "powerlol@gmail.com"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        val user = auth.currentUser
        if (user != null) {
            // get user firestore document
            val docRef = firestore.collection("users").document(user.uid).get().addOnSuccessListener {
                nickname = it.get("username").toString()
                email = it.get("email").toString()

                // update nickname and email placeholders
                rootView.findViewById<TextView>(R.id.nickname_edit_text).text = nickname
                rootView.findViewById<TextView>(R.id.email_edit_text).text = email
            }
        }

        val changeFestival = rootView.findViewById<Button>(R.id.change_festival_button)
        changeFestival.setOnClickListener {
            goToActivity(AddFestivals::class.java)
        }


        // Inflate the layout for this fragment
        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private val activityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // do something if returning to this activity
    }

    private fun goToActivity(activity: Class<*>) {
        val intent = Intent(requireContext(), activity)
        activityLauncher.launch(intent)
    }
}