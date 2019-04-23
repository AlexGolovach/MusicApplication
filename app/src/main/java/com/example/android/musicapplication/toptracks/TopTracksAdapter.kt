package com.example.android.musicapplication.toptracks

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.imageloader.Callback
import com.example.android.musicapplication.R
import com.example.android.network.models.Track
import kotlinx.android.synthetic.main.item_view_top_track.view.*

class TopTracksAdapter(private var items: List<Track> = listOf()) :
    RecyclerView.Adapter<TopTracksAdapter.ViewHolder>() {

    var playListener: PlayListener? = null
    var addListener: AddListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_view_top_track,
            parent,
            false
        )
        val holder = ViewHolder(view)

        holder.itemView.icon_play.setOnClickListener {
            val track = items[holder.adapterPosition]
            track.playcount = track.playcount + 1

            playListener?.onPlayClikedListener(track)
        }

        holder.itemView.icon_add.setOnClickListener {
            addListener?.onAddClickListener(items[holder.adapterPosition])

            holder.itemView.icon_add.setImageResource(R.drawable.ic_check)
            holder.itemView.icon_add.isClickable = false
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val artist = items[position]
        holder.bind(artist)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(list: List<Track>) {
        items = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(track: Track) {
            ImageLoader.getInstance()?.load(track.image, object : Callback {
                override fun onSuccess(url: String, bitmap: Bitmap) {
                    if (track.image == url) {
                        itemView.track_image.background = null
                        itemView.track_image.setImageBitmap(bitmap)
                    }
                }

                override fun onError(url: String, throwable: Throwable) {
                    itemView.track_image.setImageResource(R.drawable.artist_placeholder)
                }

            })
            itemView.track_name.text = track.getTrackName()
        }
    }

    interface PlayListener {
        fun onPlayClikedListener(track: Track)
    }

    interface AddListener {
        fun onAddClickListener(track: Track)
    }
}