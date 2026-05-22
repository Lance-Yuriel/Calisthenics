package com.club.calisthenics.core.data.model

import com.club.calisthenics.core.domain.model.Event
import com.club.calisthenics.core.domain.model.EventState
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import java.time.ZoneId

data class EventDto(
    @get:PropertyName("title") @set:PropertyName("title")
    var title: String = "",
    
    @get:PropertyName("description") @set:PropertyName("description")
    var description: String = "",
    
    @get:PropertyName("location") @set:PropertyName("location")
    var location: String = "",
    
    @get:PropertyName("locationUrl") @set:PropertyName("locationUrl")
    var locationUrl: String? = null,
    
    @get:PropertyName("startAt") @set:PropertyName("startAt")
    var startAt: Timestamp? = null,
    
    @get:PropertyName("endAt") @set:PropertyName("endAt")
    var endAt: Timestamp? = null,
    
    @get:PropertyName("capacity") @set:PropertyName("capacity")
    var capacity: Int = 0,
    
    @get:PropertyName("coverImageUrl") @set:PropertyName("coverImageUrl")
    var coverImageUrl: String? = null,
    
    @get:PropertyName("state") @set:PropertyName("state")
    var state: String = "DRAFT",
    
    @get:PropertyName("qrPayload") @set:PropertyName("qrPayload")
    var qrPayload: String = "",

    @get:PropertyName("attendees") @set:PropertyName("attendees")
    var attendees: List<String> = emptyList()
) {
    fun toDomain(id: String): Event {
        return Event(
            id = id,
            title = title,
            description = description,
            location = location,
            locationUrl = locationUrl,
            startAt = startAt?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDateTime() ?: java.time.LocalDateTime.now(),
            endAt = endAt?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDateTime() ?: java.time.LocalDateTime.now(),
            capacity = capacity,
            coverImageUrl = coverImageUrl,
            state = try { EventState.valueOf(state.uppercase()) } catch (e: Exception) { EventState.DRAFT },
            qrPayload = qrPayload,
            attendees = attendees
        )
    }
}
