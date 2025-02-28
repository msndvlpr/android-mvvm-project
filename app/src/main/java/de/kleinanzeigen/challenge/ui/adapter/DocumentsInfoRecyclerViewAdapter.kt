package de.kleinanzeigen.challenge.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.kleinanzeigen.challenge.data.repositpry.realestate.model.Document
import de.kleinanzeigen.challenge.databinding.DocumentViewItemBinding

/**
 * Adapter for details section of the ItemDetailsFragment
 */
class DocumentsInfoRecyclerViewAdapter (
    private val context: Context,
    private var itemsList: List<Document>
): RecyclerView.Adapter<DocumentsInfoRecyclerViewAdapter.DocumentsViewHolder>() {

    //private var itemsList: List<Document> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentsViewHolder {
        val binding = DocumentViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DocumentsViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(items: List<Document>){
        itemsList = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = itemsList.size

    override fun onBindViewHolder(viewHolder: DocumentsViewHolder, position: Int) {
        val item = itemsList[position]
        viewHolder.binding.apply {
            txtDocName.text = item.title
        }
        viewHolder.itemView.setOnClickListener {
            item.link?.let{
                downloadPdf(it)
            }
        }

    }

    private fun downloadPdf(link: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(link)
        }
        context.startActivity(intent)
    }

    inner class DocumentsViewHolder (val binding: DocumentViewItemBinding): RecyclerView.ViewHolder(binding.root)
}