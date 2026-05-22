package com.club.calisthenics.feature.events

import com.club.calisthenics.core.domain.model.Event

sealed interface EventsUiState {
    object Loading : EventsUiState
    data class Success(
        val upcomingEvents: List<Event>,
        val pastEvents: List<Event>
    ) : EventsUiState
    data class Error(val message: String) : EventsUiState
}
