package com.club.calisthenics.core.data.repository

import com.club.calisthenics.core.domain.model.Event
import com.club.calisthenics.core.domain.model.EventState
import com.club.calisthenics.core.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class MockEventRepository @Inject constructor() : EventRepository {
    private val events = MutableStateFlow(listOf(
        Event(
            id = "event_live",
            title = "Morning Flow & Handstands",
            description = "Join us for a dynamic morning session focused on handstand balance and mobility. All levels welcome!",
            location = "Central Park West",
            locationUrl = null,
            startAt = LocalDateTime.now().minusMinutes(30),
            endAt = LocalDateTime.now().plusMinutes(90),
            capacity = 15,
            coverImageUrl = "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?q=80&w=1200&h=600&auto=format&fit=crop",
            state = EventState.LIVE,
            qrPayload = "event_live",
            attendees = listOf("user_1", "user_2"),
            waitlist = emptyList()
        ),
        Event(
            id = "event_upcoming",
            title = "Muscle Up Workshop",
            description = "Master the transition. We will break down the explosive pull and the dip transition with resistance bands and technique drills.",
            location = "South Side Bars",
            locationUrl = null,
            startAt = LocalDateTime.now().plusDays(2).withHour(18).withMinute(0),
            endAt = LocalDateTime.now().plusDays(2).withHour(20).withMinute(0),
            capacity = 10,
            coverImageUrl = "https://images.unsplash.com/photo-1590239926044-037142460699?q=80&w=1200&h=600&auto=format&fit=crop",
            state = EventState.PUBLISHED,
            qrPayload = "event_upcoming",
            attendees = List(10) { "user_$it" }, // Full
            waitlist = listOf("user_waitlist_1")
        ),
        Event(
            id = "event_cancelled",
            title = "Rain Check: Planche Pro",
            description = "Session moved due to heavy rain forecast. Will reschedule for next week.",
            location = "Beach Workout Area",
            locationUrl = null,
            startAt = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0),
            endAt = LocalDateTime.now().plusDays(1).withHour(12).withMinute(0),
            capacity = 20,
            coverImageUrl = "https://images.unsplash.com/photo-1526506118085-60ce8714f8c5?q=80&w=1200&h=600&auto=format&fit=crop",
            state = EventState.CANCELLED,
            qrPayload = "event_cancelled",
            attendees = emptyList(),
            waitlist = emptyList()
        ),
        Event(
            id = "event_past",
            title = "Core & Compression",
            description = "Intensive core conditioning session.",
            location = "Main Gym",
            locationUrl = null,
            startAt = LocalDateTime.now().minusDays(5),
            endAt = LocalDateTime.now().minusDays(5).plusHours(2),
            capacity = 25,
            coverImageUrl = null,
            state = EventState.CLOSED,
            qrPayload = "event_past",
            attendees = listOf("mock_admin_123", "user_1"),
            waitlist = emptyList()
        )
    ))

    override fun getFeaturedEvent(): Flow<Event?> = events.map { list ->
        list.find { it.state == EventState.LIVE } ?: list.sortedBy { it.startAt }.find { it.state == EventState.PUBLISHED }
    }

    override fun getUpcomingEvents(): Flow<List<Event>> = events.map { list ->
        list.filter { it.state == EventState.PUBLISHED || it.state == EventState.LIVE || it.state == EventState.CANCELLED }
            .sortedBy { it.startAt }
    }

    override fun getPastEvents(): Flow<List<Event>> = events.map { list ->
        list.filter { it.state == EventState.CLOSED }.sortedByDescending { it.startAt }
    }

    override fun getEventById(id: String): Flow<Event?> = events.map { list ->
        list.find { it.id == id }
    }

    override fun getAllEvents(): Flow<List<Event>> = events

    override suspend fun createEvent(event: Event) {
        events.value += event
    }

    override suspend fun updateEvent(event: Event) {
        events.value = events.value.map { if (it.id == event.id) event else it }
    }

    override suspend fun deleteEvent(id: String) {
        events.value = events.value.filter { it.id != id }
    }

    override suspend fun updateEventState(id: String, state: EventState): Result<Unit> {
        events.value = events.value.map { if (it.id == id) it.copy(state = state) else it }
        return Result.success(Unit)
    }

    override suspend fun rsvpToEvent(eventId: String, userId: String, isAttending: Boolean): Result<Unit> {
        events.value = events.value.map { event ->
            if (event.id == eventId) {
                val attendees = event.attendees.toMutableList()
                val waitlist = event.waitlist.toMutableList()
                if (isAttending) {
                    if (attendees.size < event.capacity) attendees.add(userId) else waitlist.add(userId)
                } else {
                    attendees.remove(userId)
                    waitlist.remove(userId)
                    if (attendees.size < event.capacity && waitlist.isNotEmpty()) {
                        attendees.add(waitlist.removeAt(0))
                    }
                }
                event.copy(attendees = attendees, waitlist = waitlist)
            } else event
        }
        return Result.success(Unit)
    }

    override suspend fun checkInToEvent(eventId: String, userId: String): Result<Unit> {
        return Result.success(Unit)
    }
}
