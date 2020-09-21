package com.trelp.imgur.ui.gallery

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.trelp.imgur.R
import com.trelp.imgur.databinding.AlbumItemGalleryBinding
import com.trelp.imgur.domain.GalleryAlbum
import com.trelp.imgur.inflater
import com.trelp.imgur.visible
import timber.log.Timber

class AlbumAdapterDelegate : AdapterDelegate<MutableList<Any>>() {
    override fun isForViewType(items: MutableList<Any>, position: Int) =
        items[position] is GalleryAlbum

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        AlbumGalleryViewHolder(
            AlbumItemGalleryBinding.inflate(parent.inflater, parent, false)
        )

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as AlbumGalleryViewHolder).bind(items[position] as GalleryAlbum)

    private inner class AlbumGalleryViewHolder(
        private val binding: AlbumItemGalleryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(album: GalleryAlbum) {
            Timber.tag("bind").d("Album $adapterPosition")

            binding.apply {
                if (album.imagesCount > 1) {
                    imagesCount.text = album.imagesCount.toString()
                    type.setImageResource(R.drawable.ic_baseline_photo_album_orange_24)
                    imagesCount.visible(true)
                    type.visible(true)
                } else {
                    imagesCount.visible(false)
                    type.visible(false)
                }
                title.text = album.title
                numberOfPoints.text = album.points.toString()

                Glide.with(root.context)
                    .load(String.format("https://i.imgur.com/" + "%s.jpg", album.cover + 'l'))
                    .into(albumCover)
            }
        }
    }
}