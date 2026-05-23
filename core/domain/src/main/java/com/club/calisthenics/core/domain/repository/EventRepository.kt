package com.club.calisthenics.core.domain.repository

import com.club.calisthenics.core.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getFeaturedEvent(): Flow<Event?>
    fun getUpcomingEvents(): Flow<List<Event>>
    fun getPastEvents(): Flow<List<Event>>
    fun getEventById(id: String): Flow<Event?>
    fun getAllEvents(): Flow<List<Event>>
    suspend fun createEvent(event: Event)
    suspend fun updateEvent(event: Event)
    suspend fun deleteEvent(id: String)
    suspend fun updateEventState(id: String, state: com.club.calisthenics.core.domain.model.EventState): Result<Unit>
    suspend fun rsvpToEvent(eventId: String, userId: String, isAttending: Boolean): Result<Unit>
    suspend fun checkInToEvent(eventId: String, userId: String): Result<Unit>
}
