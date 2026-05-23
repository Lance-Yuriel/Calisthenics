package com.club.calisthenics.core.domain.model

import java.time.LocalDateTime

data class Announcement(
    val id: String,
    val title: String,
    val body: String,
    val type: AnnouncementType,
    val createdAt: LocalDateTime,
    val authorId: String,
    val actionUrl: String? = null
)

enum class AnnouncementType {
    BROADCAST, REMINDER, UPDATE, CANCELLATION
}
