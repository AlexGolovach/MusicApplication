package com.example.android.network.models

data class ArtistTrack(
    val artistName: String,
    var rating: Long,
    val track: String
) {
    fun getTrackName(): String {
        val list = listOf(artistName, track)
        return list.filter { !it.isEmpty() }.joinToString(" - ")
    }
}