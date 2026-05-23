package com.club.calisthenics.feature.events

import com.club.calisthenics.core.domain.model.Event

sealed interface EventDetailUiState {
    object Loading : EventDetailUiState
    data class Success(
        val event: Event,
        val isAttending: Boolean,
        val isWaitlisted: Boolean,
        val isFull: Boolean,
        val isAdmin: Boolean = false
    ) : EventDetailUiState
    data class Error(val message: String) : EventDetailUiState
}
