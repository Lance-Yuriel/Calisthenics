package com.club.calisthenics.core.data.repository

import android.util.Log
import com.club.calisthenics.core.data.model.UserDto
import com.club.calisthenics.core.domain.model.MemberStatus
import com.club.calisthenics.core.domain.model.User
import com.club.calisthenics.core.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUserRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    override fun getCurrentUser(userId: String): Flow<User?> = callbackFlow {
        Log.d("FirestoreUserRepo", "Fetching user document: $userId")
        val subscription = firestore.collection("users").document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FirestoreUserRepo", "Error listening to user doc", error)
                    error.printStackTrace()
                    trySend(null)
                    return@addSnapshotListener
                }
                
                if (snapshot != null && snapshot.exists()) {
                    Log.d("FirestoreUserRepo", "Snapshot found: ${snapshot.data}")
                    val userDto = snapshot.toObject(UserDto::class.java)
                    Log.d("FirestoreUserRepo", "Mapped DTO: $userDto")
                    val user = userDto?.toDomain(snapshot.id)
                    Log.d("FirestoreUserRepo", "Mapped Domain User: $user")
                    trySend(user)
                } else {
                    Log.d("FirestoreUserRepo", "No document found for UID: $userId")
                    trySend(null)
                }
            }
        awaitClose { subscription.remove() }
    }

    override fun getPendingUsers(): Flow<List<User>> = callbackFlow {
        val subscription = firestore.collection("users")
            .whereEqualTo("memberStatus", "PENDING")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    error.printStackTrace()
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val users = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(UserDto::class.java)?.toDomain(doc.id)
                } ?: emptyList()
                trySend(users)
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun createUser(user: User) {
        val dto = UserDto(
            email = user.email,
            displayName = user.displayName,
            photoUrl = user.photoUrl,
            role = user.role.name,
            memberStatus = user.memberStatus.name
        )
        firestore.collection("users").document(user.id).set(dto).await()
    }

    override suspend fun updateMemberStatus(userId: String, status: MemberStatus) {
        firestore.collection("users").document(userId)
            .update("memberStatus", status.name)
            .await()
    }

    override suspend fun updateOnboardingStatus(userId: String, seen: Boolean) {
        firestore.collection("users").document(userId)
            .update("hasSeenOnboarding", seen)
            .await()
    }
}
