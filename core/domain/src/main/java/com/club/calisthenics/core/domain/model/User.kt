package com.club.calisthenics.core.domain.model

data class User(
    val id: String,
    val email: String,
    val displayName: String?,
    val photoUrl: String?,
    val bio: String?,
    val hasSeenOnboarding: Boolean,
    val role: UserRole,
    val memberStatus: MemberStatus,
    val stats: UserStats?
)

enum class UserRole {
    MEMBER, ADMIN
}

enum class MemberStatus {
    PENDING, APPROVED, REJECTED
}
