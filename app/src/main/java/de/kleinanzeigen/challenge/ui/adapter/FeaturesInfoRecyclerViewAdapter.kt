package de.kleinanzeigen.challenge.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.kleinanzeigen.challenge.databinding.FeatureViewItemBinding

/**
 * Adapter for Features Part of the Advertisement Fragment
 */
class FeaturesInfoRecyclerViewAdapter(
    list: List<String>
) : RecyclerView.Adapter<FeaturesInfoRecyclerViewAdapter.FeaturesViewHolder>() {

    private var itemsList: List<String> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturesViewHolder {
        val binding =
            FeatureViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeaturesViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(items: List<String>) {
        itemsList = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = itemsList.size

    override fun onBindViewHolder(viewHolder: FeaturesViewHolder, position: Int) {
        val item = itemsList[position]
        viewHolder.binding.apply {
            txtFeatureName.text = item
        }
    }

    inner class FeaturesViewHolder(val binding: FeatureViewItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}