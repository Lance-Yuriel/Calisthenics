package com.club.calisthenics.core.data.repository

import com.club.calisthenics.core.domain.model.Announcement
import com.club.calisthenics.core.domain.model.AnnouncementType
import com.club.calisthenics.core.domain.repository.AnnouncementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class MockAnnouncementRepository @Inject constructor() : AnnouncementRepository {
    private val announcements = MutableStateFlow(listOf(
        Announcement(
            id = "a1",
            title = "New Equipment Arrived!",
            body = "We just installed a new set of pull-up bars and dip stations at the south park. Check them out!",
            type = AnnouncementType.BROADCAST,
            createdAt = LocalDateTime.now().minusDays(1),
            authorId = "mock_admin_123"
        ),
        Announcement(
            id = "a2",
            title = "Weather Alert: Evening Session",
            body = "Tonight's session is moved under the pavilion due to expected rain.",
            type = AnnouncementType.UPDATE,
            createdAt = LocalDateTime.now().minusHours(4),
            authorId = "mock_admin_123"
        )
    ))

    override fun getAnnouncements(): Flow<List<Announcement>> = announcements.map { it.sortedByDescending { a -> a.createdAt } }

    override fun getLatestAnnouncement(): Flow<Announcement?> = announcements.map { it.maxByOrNull { a -> a.createdAt } }

    override suspend fun createAnnouncement(announcement: Announcement): Result<Unit> {
        announcements.value += announcement
        return Result.success(Unit)
    }

    override suspend fun deleteAnnouncement(id: String): Result<Unit> {
        announcements.value = announcements.value.filter { it.id != id }
        return Result.success(Unit)
    }
}
