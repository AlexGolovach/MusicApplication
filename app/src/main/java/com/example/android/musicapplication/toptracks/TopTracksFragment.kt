package com.example.android.musicapplication.toptracks

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
import com.example.android.musicapplication.player.PlayerActivity
import com.example.android.musicapplication.utils.getAccount
import com.example.android.musicapplication.utils.showMyDialog
import com.example.android.network.Injector
import com.example.android.network.models.Track
import com.example.android.network.repository.Callback
import kotlinx.android.synthetic.main.fragment_top_tracks.*

class TopTracksFragment : BaseFragment() {

    private lateinit var topTracksAdapter: TopTracksAdapter

    override fun provideFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_top_tracks, parent, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
    }

    private fun initRecycler() {
        val context = top_tracks_recycler_view.context

        topTracksAdapter = TopTracksAdapter()

        top_tracks_recycler_view.apply {
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, VERTICAL))

            val playListener = object : TopTracksAdapter.PlayListener {

                override fun onPlayClikedListener(track: Track) {
                    val intent = Intent(activity, PlayerActivity::class.java)
                    intent.putExtra("TRACK", track.getTrackName())

                    startActivity(intent)
                }
            }

            val addListener = object : TopTracksAdapter.AddListener {
                override fun onAddClickListener(track: Track) {
                    com.example.android.database.Injector.getPlaylistRepositoryImpl()
                        .addTrackInPlaylist(getAccount()!!.userId, track.getTrackName(), track.playcount)
                }
            }

            topTracksAdapter.playListener = playListener
            topTracksAdapter.addListener = addListener

            adapter = topTracksAdapter
        }

        Injector.getTracksRepositoryImpl().getTopTracks(object :
            Callback<List<Track>> {
            override fun onSuccess(result: List<Track>) {
                topTracksAdapter.setItems(result)

                progress_bar.visibility = View.GONE
                top_tracks_recycler_view.visibility = View.VISIBLE
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