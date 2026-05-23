package com.club.calisthenics.core.data.model

import com.club.calisthenics.core.domain.model.Announcement
import com.club.calisthenics.core.domain.model.AnnouncementType
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import java.time.ZoneId

data class AnnouncementDto(
    @get:PropertyName("title") @set:PropertyName("title")
    var title: String = "",
    
    @get:PropertyName("body") @set:PropertyName("body")
    var body: String = "",
    
    @get:PropertyName("type") @set:PropertyName("type")
    var type: String = "BROADCAST",
    
    @get:PropertyName("createdAt") @set:PropertyName("createdAt")
    var createdAt: Timestamp = Timestamp.now(),
    
    @get:PropertyName("authorId") @set:PropertyName("authorId")
    var authorId: String = "",
    
    @get:PropertyName("actionUrl") @set:PropertyName("actionUrl")
    var actionUrl: String? = null
) {
    fun toDomain(id: String): Announcement {
        return Announcement(
            id = id,
            title = title,
            body = body,
            type = AnnouncementType.valueOf(type),
            createdAt = createdAt.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
            authorId = authorId,
            actionUrl = actionUrl
        )
    }

    companion object {
        fun fromDomain(domain: Announcement): AnnouncementDto {
            return AnnouncementDto(
                title = domain.title,
                body = domain.body,
                type = domain.type.name,
                createdAt = Timestamp(java.util.Date.from(domain.createdAt.atZone(ZoneId.systemDefault()).toInstant())),
                authorId = domain.authorId,
                actionUrl = domain.actionUrl
            )
        }
    }
}
