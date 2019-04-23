package com.example.android.musicapplication.topartists

import android.accounts.NetworkErrorException
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView.VERTICAL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.android.musicapplication.BaseFragment
import com.example.android.musicapplication.R
import com.example.android.musicapplication.artistdetails.ArtistActivity
import com.example.android.musicapplication.utils.showMyDialog
import com.example.android.network.Injector
import com.example.android.network.models.Artist
import com.example.android.network.repository.Callback
import kotlinx.android.synthetic.main.fragment_top_artists.*

class TopArtistsFragment : BaseFragment() {

    private lateinit var artistsAdapter: TopArtistsAdapter

    override fun provideFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_top_artists, parent, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
    }

    private fun initRecycler() {
        val context = recycler_view.context

        artistsAdapter = TopArtistsAdapter()

        recycler_view.apply {
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, VERTICAL))

            val listener = object : TopArtistsAdapter.Listener {
                override fun onItemClicked(item: Artist) {
                    val intent = Intent(activity, ArtistActivity::class.java).apply {
                        putExtra("ARTIST_NAME", item.name)
                        putExtra("ARTIST_IMAGE", item.image)
                    }

                    startActivity(intent)
                }
            }

            artistsAdapter.listener = listener

            adapter = artistsAdapter
        }

        Injector.getArtistsRepositoryImpl().getTopArtists(object :
            Callback<List<Artist>> {
            override fun onSuccess(result: List<Artist>) {
                artistsAdapter.setItems(result)

                recycler_view.visibility = View.VISIBLE
                progress_bar.visibility = View.GONE
            }

            override fun onError(throwable: Throwable) {
                if (throwable is NullPointerException) {
                    Toast.makeText(
                        activity,
                        "We have some problems with download tracks",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (throwable is NetworkErrorException) {
                    showMyDialog(activity!!)
                }
            }
        })
    }
}