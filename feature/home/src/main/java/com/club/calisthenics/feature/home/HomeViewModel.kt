package com.club.calisthenics.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.club.calisthenics.core.domain.model.Event
import com.club.calisthenics.core.domain.model.EventState
import com.club.calisthenics.core.domain.model.UserStats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            // Mocking data for now as per "Step A" instructions to build UI
            delay(1000)
            _uiState.value = HomeUiState.Success(
                featuredEvent = Event(
                    id = "1",
                    title = "Saturday Street Workout",
                    description = "Join us for a high-intensity bodyweight session. We'll focus on muscle-up progressions and pull-up volume.",
                    location = "Riverside Park",
                    locationUrl = null,
                    startAt = LocalDateTime.now().plusHours(2),
                    endAt = LocalDateTime.now().plusHours(4),
                    capacity = 20,
                    coverImageUrl = null,
                    state = EventState.PUBLISHED,
                    qrPayload = "event_1"
                ),
                stats = UserStats(
                    attendanceCount = 12,
                    badgeCount = 5
                ),
                announcement = "New skills added to the explorer! Check them out."
            )
        }
    }
}
