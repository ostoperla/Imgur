package com.trelp.imgur.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.trelp.imgur.R
import com.trelp.imgur.databinding.ImageItemGalleryBinding
import com.trelp.imgur.domain.GalleryImage
import timber.log.Timber

class ImageAdapterDelegate : AdapterDelegate<MutableList<Any>>() {
    override fun isForViewType(items: MutableList<Any>, position: Int) =
        items[position] is GalleryImage

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ImageGalleryViewHolder(
            ImageItemGalleryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ImageGalleryViewHolder).bind(items[position] as GalleryImage)

    private inner class ImageGalleryViewHolder(
        private val binding: ImageItemGalleryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: GalleryImage) {
            Timber.tag("bind").d("Image $adapterPosition, type:${image.type}")

            binding.apply {
                when (image.type) {
                    "image/gif" -> type.setImageResource(R.drawable.ic_baseline_gif_orange_24)
                    "image/png", "image/jpeg" -> type.setImageResource(R.drawable.ic_baseline_image_orange_24)
                    "video/mp4" -> type.setImageResource(R.drawable.ic_baseline_videocam_orange_24)
                }

                title.text = image.title
                numberOfPoints.text = image.points.toString()

                Glide.with(root.context)
                    .load(image.link.replace(image.id, image.id + 'l'))
                    .into(this.image)
            }
        }
    }
}