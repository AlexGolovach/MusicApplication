package com.example.android.musicapplication.topartists

import ImageLoader
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.imageloader.Callback
import com.example.android.musicapplication.R
import com.example.android.network.models.Artist
import kotlinx.android.synthetic.main.item_view_artist.view.*

class TopArtistsAdapter(private var items: List<Artist> = listOf()) :
    RecyclerView.Adapter<TopArtistsAdapter.ViewHolder>() {

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_view_artist, parent, false)
        val holder = ViewHolder(view)

        holder.itemView.setOnClickListener { listener?.onItemClicked(items[holder.adapterPosition]) }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val artist = items[position]
        holder.bind(artist)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(list: List<Artist>) {
        items = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(artist: Artist) {

            ImageLoader.getInstance()?.load(artist.image, object : Callback {
                override fun onSuccess(url: String, bitmap: Bitmap) {
                    if (artist.image == url) {
                        itemView.artist_image.background = null
                        itemView.artist_image.setImageBitmap(bitmap)
                    }
                }

                override fun onError(url: String, throwable: Throwable) {
                    itemView.artist_image.setImageResource(R.drawable.artist_placeholder)
                }

            })
            itemView.artist_name.text = artist.name
        }
    }

    interface Listener {
        fun onItemClicked(item: Artist)
    }
}