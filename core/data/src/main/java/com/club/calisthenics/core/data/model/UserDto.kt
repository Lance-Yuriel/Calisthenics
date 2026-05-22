package com.club.calisthenics.core.data.model

import com.club.calisthenics.core.domain.model.MemberStatus
import com.club.calisthenics.core.domain.model.User
import com.club.calisthenics.core.domain.model.UserRole
import com.club.calisthenics.core.domain.model.UserStats
import com.google.firebase.firestore.PropertyName

data class UserDto(
    @get:PropertyName("email") @set:PropertyName("email")
    var email: String = "",
    
    @get:PropertyName("displayName") @set:PropertyName("displayName")
    var displayName: String? = null,
    
    @get:PropertyName("photoUrl") @set:PropertyName("photoUrl")
    var photoUrl: String? = null,
    
    @get:PropertyName("bio") @set:PropertyName("bio")
    var bio: String? = null,
    
    @get:PropertyName("hasSeenOnboarding") @set:PropertyName("hasSeenOnboarding")
    var hasSeenOnboarding: Boolean = false,
    
    @get:PropertyName("role") @set:PropertyName("role")
    var role: String = "MEMBER",
    
    @get:PropertyName("memberStatus") @set:PropertyName("memberStatus")
    var memberStatus: String = "PENDING",
    
    @get:PropertyName("stats") @set:PropertyName("stats")
    var stats: UserStatsDto? = null
) {
    fun toDomain(id: String): User {
        return User(
            id = id,
            email = email,
            displayName = displayName,
            photoUrl = photoUrl,
            bio = bio,
            hasSeenOnboarding = hasSeenOnboarding,
            role = try { UserRole.valueOf(role.uppercase()) } catch (e: Exception) { UserRole.MEMBER },
            memberStatus = try { MemberStatus.valueOf(memberStatus.uppercase()) } catch (e: Exception) { MemberStatus.PENDING },
            stats = stats?.toDomain()
        )
    }
}

data class UserStatsDto(
    @get:PropertyName("attendanceCount") @set:PropertyName("attendanceCount")
    var attendanceCount: Int = 0,
    
    @get:PropertyName("badgeCount") @set:PropertyName("badgeCount")
    var badgeCount: Int = 0
) {
    fun toDomain(): UserStats {
        return UserStats(
            attendanceCount = attendanceCount,
            badgeCount = badgeCount
        )
    }
}
