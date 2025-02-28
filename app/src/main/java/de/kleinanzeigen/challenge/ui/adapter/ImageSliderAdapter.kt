package de.kleinanzeigen.challenge.ui.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import de.kleinanzeigen.challenge.R
import de.kleinanzeigen.challenge.databinding.SliderItemBinding

class ImageSliderAdapter(
    private val imageUrls: List<String>,
    private val highResImageUrls: List<String>,
    private val onImageClick: (String) -> Unit,
    private val onBitmapReady: (Bitmap?) -> Unit
) :
    RecyclerView.Adapter<ImageSliderAdapter.SliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding = SliderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(imageUrls[position], highResImageUrls[position])
    }

    override fun getItemCount(): Int = imageUrls.size

    inner class SliderViewHolder(val binding: SliderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Load the image url via Glide and bind to the ImageView, also handle the Bitmap image creation for sharing purpose
        fun bind(imageUrl: String, highResUrl: String) {
            Glide.with(binding.imgSlidePicture.context)
                .asBitmap()
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .fitCenter()
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        binding.imgSlidePicture.setImageBitmap(resource)
                        onBitmapReady(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // todo: handle clearing the resource, if needed
                    }
                })

            binding.imgSlidePicture.setOnClickListener {
                onImageClick(highResUrl)
            }
        }
    }
}
