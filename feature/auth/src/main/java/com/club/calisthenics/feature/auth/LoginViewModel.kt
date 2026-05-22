package com.club.calisthenics.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import com.club.calisthenics.core.domain.model.MemberStatus
import com.club.calisthenics.core.domain.model.User
import com.club.calisthenics.core.domain.model.UserRole
import com.club.calisthenics.core.domain.repository.AuthRepository
import com.club.calisthenics.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun login(email: String, password: String?, isSignUp: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                if (password.isNullOrBlank()) {
                    authRepository.signInAnonymously()
                } else if (isSignUp) {
                    authRepository.signUpWithEmail(email, password)
                } else {
                    authRepository.signInWithEmail(email, password)
                }

                val uid = authRepository.currentUserId
                Log.d("LoginViewModel", "Login successful. UID: $uid")
                if (uid != null) {
                    // Critical Fix: Use first() to check document existence properly without infinite loop
                    val doc = userRepository.getCurrentUser(uid).take(1).firstOrNull()
                    Log.d("LoginViewModel", "Existing document check: $doc")
                    if (doc == null) {
                        Log.d("LoginViewModel", "Creating new user document...")
                        userRepository.createUser(
                            User(
                                id = uid,
                                email = email,
                                displayName = "New Athlete",
                                photoUrl = null,
                                bio = null,
                                hasSeenOnboarding = false,
                                role = UserRole.MEMBER,
                                memberStatus = MemberStatus.PENDING,
                                stats = null
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Login error", e)
                _errorMessage.value = e.localizedMessage ?: "An unexpected error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
