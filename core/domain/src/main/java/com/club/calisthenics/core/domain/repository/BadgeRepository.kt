package com.club.calisthenics.core.domain.repository

import com.club.calisthenics.core.domain.model.Badge
import com.club.calisthenics.core.domain.model.UserBadge
import kotlinx.coroutines.flow.Flow

interface BadgeRepository {
    fun getAllBadges(): Flow<List<Badge>>
    fun getActiveBadges(): Flow<List<Badge>>
    fun getUserBadges(userId: String): Flow<List<UserBadge>>
    suspend fun createBadge(badge: Badge): Result<Unit>
    suspend fun updateBadge(badge: Badge): Result<Unit>
    suspend fun awardBadgeToUser(userId: String, badgeId: String, adminId: String, note: String?): Result<Unit>
    suspend fun revokeBadgeFromUser(userId: String, badgeId: String, adminId: String, reason: String): Result<Unit>
}
