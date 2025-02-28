package de.kleinanzeigen.challenge.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.kleinanzeigen.challenge.data.repositpry.realestate.model.Attribute
import de.kleinanzeigen.challenge.databinding.DetailsViewItemBinding

/**
 * Adapter for details section of the ItemDetailsFragment
 */
class DetailsInfoRecyclerViewAdapter (
    list: List<Attribute>
): RecyclerView.Adapter<DetailsInfoRecyclerViewAdapter.DetailsViewHolder>() {

    private var itemsList: List<Attribute> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        val binding = DetailsViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailsViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(items: List<Attribute>){
        itemsList = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = itemsList.size

    override fun onBindViewHolder(viewHolder: DetailsViewHolder, position: Int) {
        val item = itemsList[position]
        viewHolder.binding.apply {
            txtItemLabel.text = item.label
            val unit = if (item.unit.isNullOrEmpty()) "" else item.unit
            txtItemValueUnit.text = String.format("%s %s", item.value, unit)
        }
    }

    inner class DetailsViewHolder (val binding: DetailsViewItemBinding): RecyclerView.ViewHolder(binding.root)
}