package com.club.calisthenics.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUserId: String?
    val isUserLoggedIn: Flow<Boolean>
    suspend fun signInAnonymously()
    suspend fun signInWithEmail(email: String, pass: String)
    suspend fun signUpWithEmail(email: String, pass: String)
    suspend fun signOut()
}
