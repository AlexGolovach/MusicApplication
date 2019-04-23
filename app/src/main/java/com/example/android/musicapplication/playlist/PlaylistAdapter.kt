package com.example.android.musicapplication.playlist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.database.model.Playlist
import com.example.android.musicapplication.R
import com.example.android.musicapplication.utils.getRating
import kotlinx.android.synthetic.main.item_view_track_in_playlist.view.*

class PlaylistAdapter(private var items: ArrayList<Playlist> = ArrayList()) :
    RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {

    var playListener: PlayListener? = null
    var deleteListener: DeleteListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_track_in_playlist, parent, false)
        val holder = ViewHolder(view)

        holder.itemView.icon_play.setOnClickListener {
            val track = items[holder.adapterPosition]
            track.rating = track.rating + 1

            playListener?.onPlayClikedListener(track)
        }

        holder.itemView.icon_delete.setOnClickListener {
            deleteListener?.onDeleteClickedListener(items[holder.adapterPosition])
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val track = items[position]

        holder.itemView.track_name.text = track.trackName
        holder.itemView.rating.text = getRating(track.rating)
    }

    fun setItems(list: ArrayList<Playlist>) {
        items.clear()
        items.addAll(list)

        notifyDataSetChanged()
    }

    fun deleteItems(track: Playlist) {
        val iterator = items.iterator()

        while (iterator.hasNext()) {
            val item = iterator.next()

            if (track == item) {
                iterator.remove()
            }
        }

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface PlayListener {
        fun onPlayClikedListener(track: Playlist)
    }

    interface DeleteListener {
        fun onDeleteClickedListener(track: Playlist)
    }
}