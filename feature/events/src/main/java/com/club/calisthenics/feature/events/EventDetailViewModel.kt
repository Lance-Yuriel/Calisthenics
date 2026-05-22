package com.club.calisthenics.feature.events

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.club.calisthenics.core.domain.repository.AuthRepository
import com.club.calisthenics.core.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val eventId: String = checkNotNull(savedStateHandle["eventId"])

    val uiState: StateFlow<EventDetailUiState> = eventRepository.getEventById(eventId)
        .map { event ->
            if (event == null) {
                EventDetailUiState.Error("Event not found")
            } else {
                val currentUserId = authRepository.currentUserId
                EventDetailUiState.Success(
                    event = event,
                    isAttending = event.attendees.contains(currentUserId),
                    isFull = event.attendees.size >= event.capacity
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
                    isAttending = !currentState.isAttending
                )
            }
        }
    }
}
