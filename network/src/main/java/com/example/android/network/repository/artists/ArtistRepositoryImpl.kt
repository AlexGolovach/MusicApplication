package com.example.android.network.repository.artists

import android.accounts.NetworkErrorException
import com.example.android.network.LAST_FM_API_KEY
import com.example.android.network.repository.Callback
import com.example.android.network.FIRST_PART_QUERY
import com.example.android.network.httprequest.HttpRequest
import com.example.android.network.httprequest.RequestCallback
import com.example.android.network.models.Artist
import org.json.JSONObject
import java.lang.NullPointerException

class ArtistRepositoryImpl : ArtistRepository {

    private val topArtistsList = mutableListOf<Artist>()
    private val searchResultList = mutableListOf<Artist>()

    private lateinit var url: String

    override fun getTopArtists(callback: Callback<List<Artist>>) {
        topArtistsList.clear()

        url =
            "${FIRST_PART_QUERY}chart.gettopartists&api_key=$LAST_FM_API_KEY&format=json"

        HttpRequest.getInstance()?.load(url, object : RequestCallback {
            override fun onSuccess(result: String) {

                val jsonObject = JSONObject(result)
                val artistsObject = jsonObject.getJSONObject("artists")
                val artistArray = artistsObject.getJSONArray("artist")

                for (i in 0 until artistArray.length()) {
                    val artistObject = artistArray.getJSONObject(i)
                    val name = artistObject.getString("name")
                    val listeners = artistObject.getLong("listeners")

                    val imageArray = artistObject.getJSONArray("image")
                    for (j in 0 until imageArray.length()) {

                        val imageObject = imageArray.getJSONObject(j)
                        val size = imageObject.getString("size")

                        if (size == "mega") {
                            val image = imageObject.getString("#text")

                            topArtistsList.add(Artist(i, name, listeners, image))
                        }
                    }
                }

                if (topArtistsList.size != 0) {
                    callback.onSuccess(topArtistsList)
                } else {
                    callback.onError(NullPointerException("We have some problems with download tracks"))
                }
            }

            override fun onError(throwable: Throwable) {
                callback.onError(NetworkErrorException("No internet connection"))
            }

        })
    }

    override fun getArtistBio(artistName: String, callback: Callback<String>) {
        url =
            "${FIRST_PART_QUERY}artist.getinfo&artist=$artistName&api_key=$LAST_FM_API_KEY&format=json"

        HttpRequest.getInstance()?.load(url, object : RequestCallback {
            override fun onSuccess(result: String) {

                val jsonObject = JSONObject(result)
                val artistObject = jsonObject.getJSONObject("artist")
                val artistBioObject = artistObject.getJSONObject("bio")

                val artistBio = artistBioObject.getString("content")

                if (artistBio != null) {
                    callback.onSuccess(artistBio)
                } else {
                    callback.onError(NullPointerException("Artist biography not exist"))
                }
            }

            override fun onError(throwable: Throwable) {
                callback.onError(NetworkErrorException("No internet connection"))
            }

        })
    }

    override fun getSearchResult(searchTerms: String, callback: Callback<List<Artist>>) {
        searchResultList.clear()

        url =
            "${FIRST_PART_QUERY}artist.search&artist=$searchTerms&api_key=$LAST_FM_API_KEY&format=json"

        HttpRequest.getInstance()?.load(url, object : RequestCallback {
            override fun onSuccess(result: String) {

                val jsonObject = JSONObject(result)

                if (jsonObject.length() != 0) {
                    val resultsObject = jsonObject.getJSONObject("results")

                    val totalResults = resultsObject.getLong("opensearch:totalResults")

                    val artistmatchesObject = resultsObject.getJSONObject("artistmatches")

                    if (!totalResults.equals(0)) {
                        val artistArray = artistmatchesObject.getJSONArray("artist")

                        for (i in 0 until artistArray.length()) {
                            val artistObject = artistArray.getJSONObject(i)
                            val name = artistObject.getString("name")
                            val listeners = artistObject.getLong("listeners")

                            val imageArray = artistObject.getJSONArray("image")
                            for (j in 0 until imageArray.length()) {

                                val imageObject = imageArray.getJSONObject(j)
                                val size = imageObject.getString("size")

                                if (size == "mega") {
                                    val image = imageObject.getString("#text")

                                    searchResultList.add(Artist(i, name, listeners, image))
                                }
                            }
                        }
                    } else {
                        callback.onError(NullPointerException("Query result = 0"))
                    }
                    callback.onSuccess(searchResultList)
                } else {
                    callback.onError(NullPointerException("Query result = 0"))
                }
            }

            override fun onError(throwable: Throwable) {
                callback.onError(NetworkErrorException("No internet connection"))
            }

        })
    }
}