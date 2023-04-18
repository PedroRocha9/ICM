package com.example.icmproject1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.icmproject1.adapter.ArtistAdapter
import com.example.icmproject1.data.Datasource
import com.example.icmproject1.model.Stage
import org.w3c.dom.Text

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LineupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LineupFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var nStages = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_lineup, container, false)

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

        // Load data for all stages
        val day1 = Datasource(requireContext()).loadStages(2)

        // Get a reference to the RecyclerView in each StageFragment and set its adapter
        for (i in 0 until nStages) {
            val stage = childFragmentManager.findFragmentByTag("stage$i") as StageFragment
            val view = stage.rootView

            val stageData = day1[i]
            view?.findViewById<TextView>(R.id.stage_name)?.text = stageData.stageName
            val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view)
            recyclerView?.adapter = ArtistAdapter(stageData.artists)
            recyclerView?.setHasFixedSize(true)
        }
    }

    // Add fragment
    private fun addFragment(containerId: Int, fragment: Fragment, id: String? = null) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(containerId, fragment, id?: View.generateViewId().toString())
        transaction.commit()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LineupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LineupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}