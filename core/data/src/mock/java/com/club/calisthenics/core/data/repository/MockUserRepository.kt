package com.club.calisthenics.core.data.repository

import com.club.calisthenics.core.domain.model.*
import com.club.calisthenics.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MockUserRepository @Inject constructor() : UserRepository {
    private val users = MutableStateFlow(listOf(
        User(
            id = "mock_admin_123",
            email = "admin@caliclub.com",
            displayName = "Jeremy Irons",
            photoUrl = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?q=80&w=256&h=256&auto=format&fit=crop",
            bio = "Head Coach at CaliClub. Focused on Planche and Front Lever progressions. Let's get strong!",
            hasSeenOnboarding = true,
            role = UserRole.ADMIN,
            memberStatus = MemberStatus.APPROVED,
            stats = UserStats(42, 12)
        ),
        User(
            id = "user_1",
            email = "athlete_one@gmail.com",
            displayName = "Sarah Power",
            photoUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?q=80&w=256&h=256&auto=format&fit=crop",
            bio = "Passionate about bodyweight training. Working on my first pull-up!",
            hasSeenOnboarding = true,
            role = UserRole.MEMBER,
            memberStatus = MemberStatus.APPROVED,
            stats = UserStats(15, 3)
        ),
        User(
            id = "user_2",
            email = "tom_muscle@yahoo.com",
            displayName = "Tom Strong",
            photoUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?q=80&w=256&h=256&auto=format&fit=crop",
            bio = "Weighted calisthenics enthusiast.",
            hasSeenOnboarding = true,
            role = UserRole.MEMBER,
            memberStatus = MemberStatus.APPROVED,
            stats = UserStats(28, 8)
        ),
        User(
            id = "user_pending",
            email = "newbie@gmail.com",
            displayName = "Fresh Athlete",
            photoUrl = null,
            bio = null,
            hasSeenOnboarding = false,
            role = UserRole.MEMBER,
            memberStatus = MemberStatus.PENDING,
            stats = null
        )
    ))

    override fun getCurrentUser(userId: String): Flow<User?> = users.map { list ->
        list.find { it.id == userId }
    }

    override fun getPendingUsers(): Flow<List<User>> = users.map { list ->
        list.filter { it.memberStatus == MemberStatus.PENDING }
    }

    override fun getApprovedUsers(): Flow<List<User>> = users.map { list ->
        list.filter { it.memberStatus == MemberStatus.APPROVED }
    }

    override suspend fun createUser(user: User) {
        users.value += user
    }

    override suspend fun updateMemberStatus(userId: String, status: MemberStatus) {
        users.value = users.value.map {
            if (it.id == userId) it.copy(memberStatus = status) else it
        }
    }

    override suspend fun updateOnboardingStatus(userId: String, seen: Boolean) {
        users.value = users.value.map {
            if (it.id == userId) it.copy(hasSeenOnboarding = seen) else it
        }
    }

    override suspend fun updateProfile(userId: String, displayName: String?, photoUrl: String?): Result<Unit> {
        users.value = users.value.map {
            if (it.id == userId) it.copy(
                displayName = displayName ?: it.displayName,
                photoUrl = photoUrl ?: it.photoUrl
            ) else it
        }
        return Result.success(Unit)
    }
}
