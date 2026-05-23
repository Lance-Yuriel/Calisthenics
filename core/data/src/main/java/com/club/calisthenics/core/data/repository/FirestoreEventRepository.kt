package com.club.calisthenics.core.data.repository

import android.util.Log
import com.club.calisthenics.core.data.model.EventDto
import com.club.calisthenics.core.domain.model.Event
import com.club.calisthenics.core.domain.repository.EventRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject

class FirestoreEventRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : EventRepository {

    override fun getFeaturedEvent(): Flow<Event?> = callbackFlow {
        Log.d("EventRepo", "Fetching all events for Featured logic (Index-Free)")
        val subscription = firestore.collection("events")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(null)
                    return@addSnapshotListener
                }
                val events = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(EventDto::class.java)?.toDomain(doc.id)
                } ?: emptyList()
                
                // Filter and sort in memory to avoid index requirements
                val featured = events
                    .filter { it.state == com.club.calisthenics.core.domain.model.EventState.PUBLISHED || it.state == com.club.calisthenics.core.domain.model.EventState.LIVE }
                    .sortedBy { it.startAt }
                    .firstOrNull()
                
                trySend(featured)
            }
        awaitClose { subscription.remove() }
    }

    override fun getUpcomingEvents(): Flow<List<Event>> = callbackFlow {
        Log.d("EventRepo", "Fetching all events for Upcoming list (Index-Free)")
        val subscription = firestore.collection("events")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val events = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(EventDto::class.java)?.toDomain(doc.id)
                } ?: emptyList()

                val upcoming = events
                    .filter { it.state == com.club.calisthenics.core.domain.model.EventState.PUBLISHED || it.state == com.club.calisthenics.core.domain.model.EventState.LIVE }
                    .sortedBy { it.startAt }
                
                trySend(upcoming)
            }
        awaitClose { subscription.remove() }
    }

    override fun getPastEvents(): Flow<List<Event>> = callbackFlow {
        val subscription = firestore.collection("events")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val events = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(EventDto::class.java)?.toDomain(doc.id)
                } ?: emptyList()

                val past = events
                    .filter { it.state == com.club.calisthenics.core.domain.model.EventState.CLOSED || it.state == com.club.calisthenics.core.domain.model.EventState.CANCELLED }
                    .sortedByDescending { it.startAt }
                
                trySend(past)
            }
        awaitClose { subscription.remove() }
    }

    override fun getEventById(id: String): Flow<Event?> = callbackFlow {
        val subscription = firestore.collection("events").document(id)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(null)
                    return@addSnapshotListener
                }
                val event = snapshot?.toObject(EventDto::class.java)?.toDomain(snapshot.id)
                trySend(event)
            }
        awaitClose { subscription.remove() }
    }

    override fun getAllEvents(): Flow<List<Event>> = callbackFlow {
        val subscription = firestore.collection("events")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val events = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(EventDto::class.java)?.toDomain(doc.id)
                } ?: emptyList()
                trySend(events.sortedByDescending { it.startAt })
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun createEvent(event: Event) {
        val dto = EventDto(
            title = event.title,
            description = event.description,
            location = event.location,
            locationUrl = event.locationUrl,
            startAt = Timestamp(Date.from(event.startAt.atZone(ZoneId.systemDefault()).toInstant())),
            endAt = Timestamp(Date.from(event.endAt.atZone(ZoneId.systemDefault()).toInstant())),
            capacity = event.capacity,
            coverImageUrl = event.coverImageUrl,
            state = event.state.name,
            qrPayload = event.qrPayload,
            attendees = event.attendees
        )
        firestore.collection("events").add(dto).await()
    }

    override suspend fun updateEvent(event: Event) {
        val dto = EventDto(
            title = event.title,
            description = event.description,
            location = event.location,
            locationUrl = event.locationUrl,
            startAt = Timestamp(Date.from(event.startAt.atZone(ZoneId.systemDefault()).toInstant())),
            endAt = Timestamp(Date.from(event.endAt.atZone(ZoneId.systemDefault()).toInstant())),
            capacity = event.capacity,
            coverImageUrl = event.coverImageUrl,
            state = event.state.name,
            qrPayload = event.qrPayload,
            attendees = event.attendees
        )
        firestore.collection("events").document(event.id).set(dto).await()
    }

    override suspend fun deleteEvent(id: String) {
        firestore.collection("events").document(id).delete().await()
    }

    override suspend fun updateEventState(id: String, state: com.club.calisthenics.core.domain.model.EventState): Result<Unit> = try {
        firestore.collection("events").document(id).update("state", state.name).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun rsvpToEvent(eventId: String, userId: String, isAttending: Boolean): Result<Unit> = try {
        val eventRef = firestore.collection("events").document(eventId)
        if (isAttending) {
            eventRef.update("attendees", FieldValue.arrayUnion(userId)).await()
        } else {
            eventRef.update("attendees", FieldValue.arrayRemove(userId)).await()
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun checkInToEvent(eventId: String, userId: String): Result<Unit> = try {
        val attendanceId = "${eventId}_${userId}"
        val attendanceData = mapOf(
            "eventId" to eventId,
            "userId" to userId,
            "source" to "qr",
            "checkedInAt" to FieldValue.serverTimestamp()
        )
        firestore.collection("attendance").document(attendanceId).set(attendanceData).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
