package com.example.android.musicapplication.player

import android.accounts.NetworkErrorException
import android.os.Bundle
import android.widget.Toast
import com.example.android.musicapplication.R
import com.example.android.musicapplication.utils.showMyDialog
import com.example.android.network.Injector
import com.example.android.network.YOUTUBE_API_KEY
import com.example.android.network.repository.Callback
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_player.*


class PlayerActivity : YouTubeBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        initYouTubePlayer()
    }

    private fun initYouTubePlayer() {
        youtube_player.initialize(YOUTUBE_API_KEY, object : YouTubePlayer.OnInitializedListener {

            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                wasRestored: Boolean
            ) {
                val track = intent.getStringExtra("TRACK")

                Injector.getVideoRepositoryImpl().getVideoId(track, object :
                    Callback<String?> {
                    override fun onSuccess(result: String?) {
                        if (!wasRestored) {
                            player?.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                            player?.loadVideo(result)
                        } else{
                            player?.play()
                        }
                    }

                    override fun onError(throwable: Throwable) {
                        if (throwable is NullPointerException) {
                            Toast.makeText(
                                this@PlayerActivity,
                                "This video does not exist",
                                Toast.LENGTH_LONG
                            ).show()

                            finish()
                        }
                    }

                })
            }

            override fun onInitializationFailure(
                provider: YouTubePlayer.Provider?,
                errorReason: YouTubeInitializationResult?
            ) {
                if (errorReason!!.isUserRecoverableError) {
                    errorReason.getErrorDialog(this@PlayerActivity, 1).show()
                }
            }
        })
    }
}
