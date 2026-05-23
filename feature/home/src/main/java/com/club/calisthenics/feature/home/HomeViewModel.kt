package com.club.calisthenics.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.club.calisthenics.core.domain.model.UserStats
import com.club.calisthenics.core.domain.repository.AuthRepository
import com.club.calisthenics.core.domain.repository.EventRepository
import com.club.calisthenics.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        authRepository.isUserLoggedIn.flatMapLatest { isLoggedIn ->
            if (isLoggedIn) {
                val userId = authRepository.currentUserId ?: ""
                userRepository.getCurrentUser(userId)
            } else {
                flowOf(null)
            }
        },
        eventRepository.getFeaturedEvent()
    ) { user, featuredEvent ->
        Log.d("HomeViewModel", "Combining state. User: ${user?.displayName}, Event: ${featuredEvent?.title}")
        HomeUiState.Success(
            user = user,
            featuredEvent = featuredEvent,
            stats = user?.stats ?: UserStats(0, 0),
            announcement = "Welcome to Calisthenics Club!"
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState.Loading
    )

    fun checkIn(eventId: String) {
        val userId = authRepository.currentUserId ?: return
        viewModelScope.launch {
            eventRepository.checkInToEvent(eventId, userId)
        }
    }
}
