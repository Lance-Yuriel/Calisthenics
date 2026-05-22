package com.club.calisthenics.core.domain.model

import java.time.LocalDateTime

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val location: String,
    val locationUrl: String?,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val capacity: Int,
    val coverImageUrl: String?,
    val state: EventState,
    val qrPayload: String
)

enum class EventState {
    DRAFT, PUBLISHED, LIVE, CLOSED, CANCELLED, ARCHIVED
}
