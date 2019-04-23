package com.example.android.network.repository.tracks

import android.accounts.NetworkErrorException
import com.example.android.network.LAST_FM_API_KEY
import com.example.android.network.FIRST_PART_QUERY
import com.example.android.network.httprequest.HttpRequest
import com.example.android.network.httprequest.RequestCallback
import com.example.android.network.models.ArtistTrack
import com.example.android.network.models.Track
import com.example.android.network.repository.Callback
import org.json.JSONObject
import java.lang.NullPointerException

class TracksRepositoryImpl : TracksRepository {

    private val topTracksList = mutableListOf<Track>()
    private val artistTopTracksList = mutableListOf<ArtistTrack>()

    private lateinit var url: String

    override fun getArtistTopTracks(artistName: String, callback: Callback<List<ArtistTrack>>) {
        artistTopTracksList.clear()

        url =
            "${FIRST_PART_QUERY}artist.gettoptracks&artist=$artistName&api_key=$LAST_FM_API_KEY&format=json"

        HttpRequest.getInstance()?.load(url, object : RequestCallback {
            override fun onSuccess(result: String) {

                val jsonObject = JSONObject(result)
                val topTracksObject = jsonObject.getJSONObject("toptracks")
                val tracksArray = topTracksObject.getJSONArray("track")

                for (i in 0 until tracksArray.length()) {
                    val trackObject = tracksArray.getJSONObject(i)
                    val track = trackObject.getString("name")
                    val rating = trackObject.getLong("playcount")

                    artistTopTracksList.add(ArtistTrack(artistName, rating, track))
                }

                if (artistTopTracksList.size != 0) {
                    callback.onSuccess(artistTopTracksList)
                } else {
                    callback.onError(NullPointerException("We have some problems with download tracks"))
                }
            }

            override fun onError(throwable: Throwable) {
                callback.onError(NetworkErrorException("No internet connection"))
            }

        })
    }

    override fun getTopTracks(callback: Callback<List<Track>>) {
        topTracksList.clear()

        url =
            "${FIRST_PART_QUERY}chart.gettoptracks&api_key=$LAST_FM_API_KEY&format=json"

        HttpRequest.getInstance()?.load(url, object : RequestCallback {
            override fun onSuccess(result: String) {

                val jsonObject = JSONObject(result)
                val tracksObject = jsonObject.getJSONObject("tracks")
                val trackArray = tracksObject.getJSONArray("track")

                for (i in 0 until trackArray.length()) {
                    val trackObject = trackArray.getJSONObject(i)
                    val track = trackObject.getString("name")
                    val playcount = trackObject.getLong("playcount")

                    val artistObject = trackObject.getJSONObject("artist")

                    val artistName = artistObject.getString("name")

                    val imageArray = trackObject.getJSONArray("image")
                    for (j in 0 until imageArray.length()) {

                        val imageObject = imageArray.getJSONObject(j)
                        val size = imageObject.getString("size")

                        if (size == "extralarge") {
                            val image = imageObject.getString("#text")

                            topTracksList.add(Track(i, artistName, track, playcount, image))
                        }
                    }
                }

                if (topTracksList.size != 0) {
                    callback.onSuccess(topTracksList)
                } else {
                    callback.onError(NullPointerException("We have some problems with download tracks"))
                }
            }

            override fun onError(throwable: Throwable) {
                callback.onError(NetworkErrorException("No internet connection"))
            }

        })
    }
}