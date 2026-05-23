package com.club.calisthenics.core.domain.repository

import com.club.calisthenics.core.domain.model.Announcement
import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {
    fun getAnnouncements(): Flow<List<Announcement>>
    fun getLatestAnnouncement(): Flow<Announcement?>
    suspend fun createAnnouncement(announcement: Announcement): Result<Unit>
    suspend fun deleteAnnouncement(id: String): Result<Unit>
}
