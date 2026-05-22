package com.club.calisthenics.feature.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.club.calisthenics.core.domain.model.User
import com.club.calisthenics.core.domain.repository.AuthRepository
import com.club.calisthenics.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val user: StateFlow<User?> = authRepository.isUserLoggedIn.flatMapLatest { isLoggedIn ->
        if (isLoggedIn) {
            val userId = authRepository.currentUserId ?: ""
            Log.d("ProfileViewModel", "User is logged in. Fetching data for UID: $userId")
            userRepository.getCurrentUser(userId)
        } else {
            Log.d("ProfileViewModel", "User is NOT logged in.")
            flowOf(null)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }
}
