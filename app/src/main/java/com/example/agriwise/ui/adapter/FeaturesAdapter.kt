package com.example.agriwise.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.agriwise.R
import com.example.agriwise.data.model.FeaturesData
import java.util.*

class FeaturesAdapter(private val onClickCallBack: (Int)->Unit) :
    ListAdapter<FeaturesData, FeaturesAdapter.ViewHolder>(FeaturesDataDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.features_layout, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder. itemView.findViewById<CardView>(R.id.card_view).setOnClickListener {
            onClickCallBack(position)
        }

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: FeaturesData) = with(itemView) {
            // TODO: Bind the data with View
            val img = itemView.findViewById<ImageView>(R.id.image)
            val name = itemView.findViewById<TextView>(R.id.name)
            val description = itemView.findViewById<TextView>(R.id.description)
            img.setImageResource(item.img)
            name.text=item.name
            description.text = item.description
            itemView.findViewById<CardView>(R.id.card_view).setOnClickListener {

            }



        }
    }


}

class FeaturesDataDiffCallback : DiffUtil.ItemCallback<FeaturesData>() {
    override fun areItemsTheSame(oldItem: FeaturesData, newItem: FeaturesData): Boolean {
        //    example  return oldItem.nightId == newItem.nightId
// check with any value of the item
       return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: FeaturesData, newItem: FeaturesData): Boolean {
        return oldItem == newItem
    }
}