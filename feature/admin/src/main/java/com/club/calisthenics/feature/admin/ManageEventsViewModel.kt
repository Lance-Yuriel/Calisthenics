package com.club.calisthenics.feature.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.club.calisthenics.core.domain.model.Event
import com.club.calisthenics.core.domain.model.EventState
import com.club.calisthenics.core.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageEventsViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    val allEvents: StateFlow<List<Event>> = eventRepository.getAllEvents()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteEvent(id: String) {
        viewModelScope.launch {
            eventRepository.deleteEvent(id)
        }
    }

    fun updateEventState(id: String, state: EventState) {
        viewModelScope.launch {
            eventRepository.updateEventState(id, state)
        }
    }
}
