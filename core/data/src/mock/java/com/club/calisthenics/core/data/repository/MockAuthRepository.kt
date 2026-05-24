package com.club.calisthenics.core.data.repository

import com.club.calisthenics.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class MockAuthRepository @Inject constructor() : AuthRepository {
    private val _isLoggedIn = MutableStateFlow(true)
    override val isUserLoggedIn: Flow<Boolean> = _isLoggedIn
    override val currentUserId: String? = "mock_admin_123"

    override suspend fun signInWithEmail(email: String, pass: String) {
        _isLoggedIn.value = true
    }

    override suspend fun signUpWithEmail(email: String, pass: String) {
        _isLoggedIn.value = true
    }

    override suspend fun signInAnonymously() {
        _isLoggedIn.value = true
    }

    override suspend fun signOut() {
        _isLoggedIn.value = false
    }
}
