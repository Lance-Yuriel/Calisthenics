package com.club.calisthenics.core.data.model

import com.club.calisthenics.core.domain.model.Badge
import com.club.calisthenics.core.domain.model.UserBadge
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import java.time.ZoneId

data class BadgeDto(
    @get:PropertyName("name") @set:PropertyName("name")
    var name: String = "",
    @get:PropertyName("description") @set:PropertyName("description")
    var description: String = "",
    @get:PropertyName("imageUrl") @set:PropertyName("imageUrl")
    var imageUrl: String? = null,
    @get:PropertyName("isActive") @set:PropertyName("isActive")
    var isActive: Boolean = true
) {
    fun toDomain(id: String): Badge = Badge(
        id = id,
        name = name,
        description = description,
        imageUrl = imageUrl,
        isActive = isActive
    )
}

data class UserBadgeDto(
    @get:PropertyName("userId") @set:PropertyName("userId")
    var userId: String = "",
    @get:PropertyName("badgeId") @set:PropertyName("badgeId")
    var badgeId: String = "",
    @get:PropertyName("earnedAt") @set:PropertyName("earnedAt")
    var earnedAt: Timestamp? = null,
    @get:PropertyName("awardedByAdminId") @set:PropertyName("awardedByAdminId")
    var awardedByAdminId: String? = null,
    @get:PropertyName("note") @set:PropertyName("note")
    var note: String? = null
) {
    fun toDomain(id: String): UserBadge = UserBadge(
        id = id,
        userId = userId,
        badgeId = badgeId,
        earnedAt = earnedAt?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDateTime() ?: java.time.LocalDateTime.now(),
        awardedByAdminId = awardedByAdminId,
        note = note
    )
}
