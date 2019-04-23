package com.example.android.database.repository.playlist

import android.widget.Toast
import com.example.android.database.App
import com.example.android.database.Callback
import com.example.android.database.model.Playlist
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.NullPointerException

class PlaylistRepositoryImpl : PlaylistRepository {

    private val myDB = FirebaseFirestore.getInstance()
    private val playlist = myDB.collection("Playlist")
    private val trackList: ArrayList<Playlist> = ArrayList()

    override fun addTrackInPlaylist(userId: String, track: String, rating: Long) {
        val trackId = playlist.document().id

        playlist
            .whereEqualTo("trackName", track)
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    playlist.add(
                        mapOf(
                            "trackId" to trackId,
                            "userId" to userId,
                            "rating" to rating,
                            "trackName" to track
                        )
                    )
                } else {
                    it.forEach { queryDocumentSnapshot ->
                        playlist.document(queryDocumentSnapshot.id).update(
                            "rating", rating
                        )
                    }
                }
            }
    }

    override fun downloadPlaylist(userdId: String, callback: Callback<ArrayList<Playlist>>) {
        trackList.clear()

        playlist
            .whereEqualTo("userId", userdId)
            .get()
            .addOnSuccessListener {
                it.forEach { queryDocumentSnapshot ->
                    val trackId = queryDocumentSnapshot.id
                    val userId = queryDocumentSnapshot.get("userId").toString()
                    val rating = queryDocumentSnapshot.get("rating") as Long
                    val track = queryDocumentSnapshot.get("trackName").toString()

                    trackList.add(Playlist(trackId, userId, rating, track))
                    trackList.sortBy { item ->
                        item.rating
                    }

                    if (trackList.size != 0) {
                        callback.onSuccess(trackList)
                    } else {
                        callback.onError(NullPointerException("No tracks in playlist"))
                    }
                }
            }
    }

    override fun deleteTrack(track: Playlist) {

        playlist.document(track.trackId).delete().addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(App.get(), "Successfully deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(App.get(), "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun updateRating(trackId: String, rating: Long) {
        playlist.document(trackId).update(
            "rating", rating
        )
    }

}