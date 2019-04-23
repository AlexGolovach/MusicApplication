package com.example.android.musicapplication.playlist

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView.VERTICAL
import android.view.View
import com.example.android.database.Callback
import com.example.android.database.Injector
import com.example.android.database.model.Playlist
import com.example.android.musicapplication.R
import com.example.android.musicapplication.player.PlayerActivity
import com.example.android.musicapplication.utils.getAccount
import kotlinx.android.synthetic.main.activity_playlist.*

class PlaylistActivity : AppCompatActivity() {

    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_playlist)

        initRecycler()
    }

    private fun initRecycler() {
        val context = playlist_recycler_view.context

        playlistAdapter = PlaylistAdapter()

        Injector.getPlaylistRepositoryImpl()
            .downloadPlaylist(
                getAccount()!!.userId,
                object : Callback<ArrayList<Playlist>> {
                    override fun onSuccess(result: ArrayList<Playlist>) {
                        playlistAdapter.setItems(result)

                        progress_bar.visibility = View.GONE
                        playlist_recycler_view.visibility = View.VISIBLE
                    }

                    override fun onError(throwable: Throwable) {
                        if (throwable is NullPointerException){
                            progress_bar.visibility = View.GONE
                            empty_playlist.visibility = View.VISIBLE
                        }
                    }
                })

        playlist_recycler_view.apply {
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, VERTICAL))

            val playListener = object : PlaylistAdapter.PlayListener {

                override fun onPlayClikedListener(track: Playlist) {
                    val intent = Intent(this@PlaylistActivity, PlayerActivity::class.java)
                    intent.putExtra("TRACK", track.trackName)

                    startActivity(intent)

                    Injector.getPlaylistRepositoryImpl().updateRating(track.trackId, track.rating)
                }
            }

            val deleteListener = object : PlaylistAdapter.DeleteListener{
                override fun onDeleteClickedListener(track: Playlist) {
                    playlistAdapter.deleteItems(track)

                    Injector.getPlaylistRepositoryImpl().deleteTrack(track)
                }
            }

            playlistAdapter.playListener = playListener
            playlistAdapter.deleteListener = deleteListener

            adapter = playlistAdapter
        }
    }
}