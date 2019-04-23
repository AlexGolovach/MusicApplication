package com.example.android.network.models

data class Track(
    val id: Int,
    val artistName: String,
    val track: String,
    var playcount: Long,
    val image: String
){
    fun getTrackName(): String {
        val list = listOf(artistName,track)
        return list.filter { !it.isEmpty() }.joinToString(" - ")
    }
}