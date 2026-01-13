package com.jycra.filmaico.data.channel.data.service

import com.google.firebase.firestore.FirebaseFirestore
import com.jycra.filmaico.core.model.channel.ChannelCarouselDto
import com.jycra.filmaico.core.model.channel.ChannelDto
import javax.inject.Inject

class ChannelService @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun getChannelCarouselsRealtime(onUpdate: (List<ChannelCarouselDto>) -> Unit) {
        firestore.collection("channel_carousels")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val carousels = snapshot.documents.mapNotNull { document ->
                        document.toObject(ChannelCarouselDto::class.java)
                    }
                    onUpdate(carousels)
                }
            }
    }

    fun getChannelsRealtime(onUpdate: (List<ChannelDto>) -> Unit) {
        firestore.collection("channels")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val channels = snapshot.documents.mapNotNull { document ->
                        document.toObject(ChannelDto::class.java)
                    }
                    onUpdate(channels)
                }
            }
    }

}