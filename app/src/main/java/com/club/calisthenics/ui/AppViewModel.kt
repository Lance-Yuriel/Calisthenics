package com.club.calisthenics.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.club.calisthenics.core.domain.model.User
import com.club.calisthenics.core.domain.repository.AuthRepository
import com.club.calisthenics.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AppViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _isInitializing = MutableStateFlow(true)
    val isInitializing = _isInitializing.asStateFlow()

    val currentUser: StateFlow<User?> = authRepository.isUserLoggedIn
        .flatMapLatest { isLoggedIn ->
            if (isLoggedIn) {
                val uid = authRepository.currentUserId ?: ""
                userRepository.getCurrentUser(uid)
            } else {
                flowOf(null)
            }
        }
        .onStart {
            delay(1500)
            _isInitializing.value = false
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val isUserLoggedIn: StateFlow<Boolean> = authRepository.isUserLoggedIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun completeOnboarding() {
        val uid = authRepository.currentUserId ?: return
        viewModelScope.launch {
            userRepository.updateOnboardingStatus(uid, true)
        }
    }

    fun reApply() {
        val uid = authRepository.currentUserId ?: return
        viewModelScope.launch {
            userRepository.updateMemberStatus(uid, com.club.calisthenics.core.domain.model.MemberStatus.PENDING)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }
}
