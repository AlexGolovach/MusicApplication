package com.example.android.musicapplication.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.musicapplication.R
import com.example.android.network.models.Artist
import kotlinx.android.synthetic.main.item_view_search_result.view.*

class SearchResultsAdapter(private var items: List<Artist> = listOf()) :
    RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_view_search_result,
            parent,
            false
        )
        val holder = ViewHolder(view)

        holder.itemView.container.setOnClickListener {
            listener?.onItemClickListener(items[holder.adapterPosition])
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val artist = items[position]

        holder. itemView.artist_name.text = artist.name
    }

    fun update(list: List<Artist>){
        items = emptyList()
        items = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface Listener {
        fun onItemClickListener(artist: Artist)
    }
}