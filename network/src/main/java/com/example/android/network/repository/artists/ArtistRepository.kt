package com.example.android.network.repository.artists

import com.example.android.network.repository.Callback
import com.example.android.network.models.Artist

interface ArtistRepository {

    fun getTopArtists(callback: Callback<List<Artist>>)

    fun getArtistBio(artistName: String, callback: Callback<String>)

    fun getSearchResult(searchTerms: String, callback: Callback<List<Artist>>)
}