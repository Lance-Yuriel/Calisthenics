package com.club.calisthenics.core.domain.model

import java.time.LocalDateTime

data class Badge(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val isActive: Boolean = true
)

data class UserBadge(
    val id: String, // userId_badgeId
    val userId: String,
    val badgeId: String,
    val earnedAt: LocalDateTime,
    val awardedByAdminId: String?,
    val note: String?,
    val badge: Badge? = null // Joined data for convenience
)
