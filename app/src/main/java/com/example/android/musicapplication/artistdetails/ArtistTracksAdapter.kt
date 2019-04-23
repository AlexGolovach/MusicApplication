package com.example.android.musicapplication.artistdetails

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.database.App
import com.example.android.musicapplication.R
import com.example.android.musicapplication.utils.getRating
import com.example.android.network.models.ArtistTrack
import kotlinx.android.synthetic.main.item_view_track.view.*

class ArtistTracksAdapter(private var items: List<ArtistTrack> = listOf()) :
    RecyclerView.Adapter<ArtistTracksAdapter.ViewHolder>() {

    var playListener: PlayListener? = null
    var addListener: AddListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_view_track,
            parent,
            false
        )
        val holder = ViewHolder(view)

        holder.itemView.icon_play.setOnClickListener {
            val track = items[holder.adapterPosition]
            track.rating = track.rating + 1

            playListener?.onPlayClickedListener(track)
        }

        holder.itemView.icon_add.setOnClickListener {
            addListener?.onAddClickedListener(items[holder.adapterPosition])

            holder.itemView.icon_add.setImageResource(R.drawable.ic_check)
            holder.itemView.icon_add.isClickable = false
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val track = items[position]

        holder.itemView.track_name.text = track.getTrackName()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(list: List<ArtistTrack>){
        items = list
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface PlayListener{
        fun onPlayClickedListener(item: ArtistTrack)
    }

    interface AddListener{
        fun onAddClickedListener(item: ArtistTrack)
    }
}