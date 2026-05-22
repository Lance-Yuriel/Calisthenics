package com.club.calisthenics.feature.events

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.club.calisthenics.core.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    val uiState: StateFlow<EventsUiState> = combine(
        eventRepository.getUpcomingEvents(),
        eventRepository.getPastEvents()
    ) { upcoming, past ->
        Log.d("EventsViewModel", "Upcoming: ${upcoming.size}, Past: ${past.size}")
        EventsUiState.Success(upcoming, past)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EventsUiState.Loading
    )
}
