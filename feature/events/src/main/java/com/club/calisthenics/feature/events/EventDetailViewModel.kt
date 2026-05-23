package com.club.calisthenics.feature.events

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.club.calisthenics.core.domain.model.UserRole
import com.club.calisthenics.core.domain.repository.AuthRepository
import com.club.calisthenics.core.domain.repository.EventRepository
import com.club.calisthenics.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val eventId: String = checkNotNull(savedStateHandle["eventId"])

    val uiState: StateFlow<EventDetailUiState> = combine(
        eventRepository.getEventById(eventId),
        authRepository.currentUserId?.let { userRepository.getCurrentUser(it) } ?: flowOf(null)
    ) { event, user ->
        if (event == null) {
            EventDetailUiState.Error("Event not found")
        } else {
            EventDetailUiState.Success(
                event = event,
                isAttending = event.attendees.contains(user?.id),
                isWaitlisted = event.waitlist.contains(user?.id),
                isFull = event.attendees.size >= event.capacity,
                isAdmin = user?.role == UserRole.ADMIN
            )
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = EventDetailUiState.Loading
        )

    fun toggleRsvp() {
        val currentUserId = authRepository.currentUserId ?: return
        val currentState = uiState.value
        if (currentState is EventDetailUiState.Success) {
            viewModelScope.launch {
                eventRepository.rsvpToEvent(
                    eventId = eventId,
                    userId = currentUserId,
                    isAttending = !(currentState.isAttending || currentState.isWaitlisted)
                )
            }
        }
    }

    fun checkIn() {
        val currentUserId = authRepository.currentUserId ?: return
        viewModelScope.launch {
            eventRepository.checkInToEvent(eventId, currentUserId)
        }
    }
}
