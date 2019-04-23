package com.example.android.musicapplication.artistdetails

import ImageLoader
import android.accounts.NetworkErrorException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView.VERTICAL
import android.view.View
import android.widget.Toast
import com.example.android.imageloader.Callback
import com.example.android.musicapplication.R
import com.example.android.musicapplication.player.PlayerActivity
import com.example.android.musicapplication.utils.getAccount
import com.example.android.musicapplication.utils.showMyDialog
import com.example.android.network.Injector
import com.example.android.network.models.ArtistTrack
import kotlinx.android.synthetic.main.activity_artist.*

class ArtistActivity : AppCompatActivity() {

    private lateinit var tracksAdapter: ArtistTracksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist)

        getArtistInformation()
        initRecycler()
    }

    private fun getArtistInformation() {
        val artistImage = intent.getStringExtra("ARTIST_IMAGE")
        val artistName = intent.getStringExtra("ARTIST_NAME")

        Injector.getArtistsRepositoryImpl().getArtistBio(
            artistName,
            object : com.example.android.network.repository.Callback<String> {
                override fun onSuccess(result: String) {
                    artist_bio.text = result
                }

                override fun onError(throwable: Throwable) {
                    if (throwable is NullPointerException) {
                        artist_bio.text = getString(R.string.biography_not_exist)
                    } else if (throwable is NetworkErrorException) {
                        showMyDialog(this@ArtistActivity)
                    }
                }
            })

        if (artistImage.isEmpty()){
            getArtistImage("https://upload.wikimedia.org/wikipedia/commons/3/3c/No-album-art.png")
        } else{
            getArtistImage(artistImage)
        }
    }

    private fun getArtistImage(artistImage: String){
        ImageLoader.getInstance()?.load(artistImage, object : Callback {
            override fun onSuccess(url: String, bitmap: Bitmap) {
                if (artistImage == url) {
                    artist_image.setImageBitmap(bitmap)
                }
            }

            override fun onError(url: String, throwable: Throwable) {
                artist_image.setImageResource(R.drawable.artist_placeholder)
            }
        })
    }

    private fun initRecycler() {
        val artistName = intent.getStringExtra("ARTIST_NAME")
        val context = tracks_recycler_view.context

        tracksAdapter = ArtistTracksAdapter()

        Injector.getTracksRepositoryImpl().getArtistTopTracks(
            artistName,
            object : com.example.android.network.repository.Callback<List<ArtistTrack>> {
                override fun onSuccess(result: List<ArtistTrack>) {
                    tracksAdapter.setItems(result)

                    progress_bar.visibility = View.GONE
                    artist_image.visibility = View.VISIBLE
                    artist_bio.visibility = View.VISIBLE
                    tracks_recycler_view.visibility = View.VISIBLE
                }

                override fun onError(throwable: Throwable) {
                    if (throwable is NullPointerException) {
                        Toast.makeText(
                            this@ArtistActivity,
                            "We have some problems with download tracks",
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (throwable is NetworkErrorException) {
                        showMyDialog(this@ArtistActivity)
                    }
                }

            })

        tracks_recycler_view.apply {
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, VERTICAL))

            val playListener = object : ArtistTracksAdapter.PlayListener {
                override fun onPlayClickedListener(item: ArtistTrack) {
                    val intent = Intent(this@ArtistActivity, PlayerActivity::class.java)
                    intent.putExtra("TRACK", item.getTrackName())

                    startActivity(intent)
                }
            }

            val addListener = object : ArtistTracksAdapter.AddListener {
                override fun onAddClickedListener(item: ArtistTrack) {
                    com.example.android.database.Injector.getPlaylistRepositoryImpl()
                        .addTrackInPlaylist(getAccount()!!.userId, item.getTrackName(), item.rating)
                }
            }

            tracksAdapter.playListener = playListener
            tracksAdapter.addListener = addListener

            adapter = tracksAdapter
        }
    }
}