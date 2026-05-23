package com.club.calisthenics.core.data.repository

import com.club.calisthenics.core.data.model.AnnouncementDto
import com.club.calisthenics.core.domain.model.Announcement
import com.club.calisthenics.core.domain.repository.AnnouncementRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreAnnouncementRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : AnnouncementRepository {

    override fun getAnnouncements(): Flow<List<Announcement>> = callbackFlow {
        val subscription = firestore.collection("announcements")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val announcements = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(AnnouncementDto::class.java)?.toDomain(doc.id)
                } ?: emptyList()
                trySend(announcements)
            }
        awaitClose { subscription.remove() }
    }

    override fun getLatestAnnouncement(): Flow<Announcement?> = callbackFlow {
        val subscription = firestore.collection("announcements")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(1)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(null)
                    return@addSnapshotListener
                }
                val announcement = snapshot?.documents?.firstOrNull()?.let { doc ->
                    doc.toObject(AnnouncementDto::class.java)?.toDomain(doc.id)
                }
                trySend(announcement)
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun createAnnouncement(announcement: Announcement): Result<Unit> = try {
        val dto = AnnouncementDto.fromDomain(announcement)
        firestore.collection("announcements").add(dto).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteAnnouncement(id: String): Result<Unit> = try {
        firestore.collection("announcements").document(id).delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
