package com.club.calisthenics.core.data.repository

import com.club.calisthenics.core.domain.model.Badge
import com.club.calisthenics.core.domain.model.UserBadge
import com.club.calisthenics.core.domain.repository.BadgeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class MockBadgeRepository @Inject constructor() : BadgeRepository {
    private val badges = MutableStateFlow(listOf(
        Badge("b1", "Early Bird", "Attended a session before 8 AM", "https://img.icons8.com/color/512/sun.png", true),
        Badge("b2", "Powerhouse", "Mastered the Muscle Up", "https://img.icons8.com/color/512/weightlifting.png", true),
        Badge("b3", "Static King", "Held a Front Lever for 10 seconds", "https://img.icons8.com/color/512/stretching.png", true),
        Badge("b4", "Century Club", "Attended 100 sessions", "https://img.icons8.com/color/512/trophy.png", true)
    ))

    private val userBadges = MutableStateFlow(listOf(
        UserBadge("ub1", "mock_admin_123", "b1", LocalDateTime.now().minusMonths(1), "admin", "First badge!", Badge("b1", "Early Bird", "", null, true)),
        UserBadge("ub2", "mock_admin_123", "b2", LocalDateTime.now().minusDays(10), "admin", "Solid effort", Badge("b2", "Powerhouse", "", null, true))
    ))

    override fun getAllBadges(): Flow<List<Badge>> = badges
    override fun getActiveBadges(): Flow<List<Badge>> = badges.map { list -> list.filter { it.isActive } }
    override fun getUserBadges(userId: String): Flow<List<UserBadge>> = userBadges.map { list -> list.filter { it.userId == userId } }

    override suspend fun createBadge(badge: Badge): Result<Unit> {
        badges.value += badge
        return Result.success(Unit)
    }

    override suspend fun updateBadge(badge: Badge): Result<Unit> {
        badges.value = badges.value.map { if (it.id == badge.id) badge else it }
        return Result.success(Unit)
    }

    override suspend fun awardBadgeToUser(userId: String, badgeId: String, adminId: String, note: String?): Result<Unit> {
        val badge = badges.value.find { it.id == badgeId } ?: return Result.failure(Exception("Not found"))
        userBadges.value += UserBadge("${userId}_${badgeId}", userId, badgeId, LocalDateTime.now(), adminId, note, badge)
        return Result.success(Unit)
    }

    override suspend fun revokeBadgeFromUser(userId: String, badgeId: String, adminId: String, reason: String): Result<Unit> {
        userBadges.value = userBadges.value.filterNot { it.userId == userId && it.badgeId == badgeId }
        return Result.success(Unit)
    }
}
