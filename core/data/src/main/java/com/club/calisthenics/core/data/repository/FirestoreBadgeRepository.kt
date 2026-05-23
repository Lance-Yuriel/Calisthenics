package com.club.calisthenics.core.data.repository

import com.club.calisthenics.core.data.model.BadgeDto
import com.club.calisthenics.core.data.model.UserBadgeDto
import com.club.calisthenics.core.domain.model.Badge
import com.club.calisthenics.core.domain.model.UserBadge
import com.club.calisthenics.core.domain.repository.BadgeRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class FirestoreBadgeRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : BadgeRepository {

    override fun getAllBadges(): Flow<List<Badge>> = callbackFlow {
        val subscription = firestore.collection("badges")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val badges = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(BadgeDto::class.java)?.toDomain(doc.id)
                } ?: emptyList()
                trySend(badges)
            }
        awaitClose { subscription.remove() }
    }

    override fun getActiveBadges(): Flow<List<Badge>> = getAllBadges().map { badges ->
        badges.filter { it.isActive }
    }

    override fun getUserBadges(userId: String): Flow<List<UserBadge>> = callbackFlow {
        val subscription = firestore.collection("userBadges")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val userBadges = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(UserBadgeDto::class.java)?.toDomain(doc.id)
                } ?: emptyList()
                trySend(userBadges)
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun createBadge(badge: Badge): Result<Unit> = try {
        val dto = BadgeDto(
            name = badge.name,
            description = badge.description,
            imageUrl = badge.imageUrl,
            isActive = badge.isActive
        )
        firestore.collection("badges").add(dto).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateBadge(badge: Badge): Result<Unit> = try {
        val dto = BadgeDto(
            name = badge.name,
            description = badge.description,
            imageUrl = badge.imageUrl,
            isActive = badge.isActive
        )
        firestore.collection("badges").document(badge.id).set(dto).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun awardBadgeToUser(userId: String, badgeId: String, adminId: String, note: String?): Result<Unit> = try {
        val docId = "${userId}_${badgeId}"
        val dto = UserBadgeDto(
            userId = userId,
            badgeId = badgeId,
            earnedAt = Timestamp.now(),
            awardedByAdminId = adminId,
            note = note
        )
        firestore.collection("userBadges").document(docId).set(dto).await()
        // Update user stats denormalized count
        firestore.collection("users").document(userId)
            .update("stats.badgeCount", FieldValue.increment(1))
            .await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun revokeBadgeFromUser(userId: String, badgeId: String, adminId: String, reason: String): Result<Unit> = try {
        val docId = "${userId}_${badgeId}"
        firestore.collection("userBadges").document(docId).delete().await()
        // Update user stats denormalized count
        firestore.collection("users").document(userId)
            .update("stats.badgeCount", FieldValue.increment(-1))
            .await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
