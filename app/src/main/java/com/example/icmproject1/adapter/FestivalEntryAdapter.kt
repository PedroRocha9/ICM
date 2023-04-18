package com.example.icmproject1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.icmproject1.R
import com.example.icmproject1.model.FestivalEntry
class FestivalEntryAdapter(
    val dataset: List<FestivalEntry>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<FestivalEntryAdapter.ItemViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just an Affirmation object.
    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        val name : TextView = view.findViewById(R.id.festival_title)
        val location : TextView = view.findViewById(R.id.festival_location)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(v, position)
            }
        }
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.festival_entry_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount() = dataset.size

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.name.text = item.name
        holder.location.text = item.location
    }
}
