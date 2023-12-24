package ru.smak.qrmaps.database

import java.time.LocalDateTime

data class ExtendedTrackInfo(
    val id: Long,

    val owner: Boolean,

    val started: LocalDateTime? = null,

    val finished: LocalDateTime? = null,

    val length: Float = 0f
){
    fun toTrack() = Track(id, owner)
}