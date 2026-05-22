package com.club.calisthenics.core.domain.repository

import com.club.calisthenics.core.domain.model.MemberStatus
import com.club.calisthenics.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getCurrentUser(userId: String): Flow<User?>
    fun getPendingUsers(): Flow<List<User>>
    suspend fun createUser(user: User)
    suspend fun updateMemberStatus(userId: String, status: MemberStatus)
    suspend fun updateOnboardingStatus(userId: String, seen: Boolean)
}
