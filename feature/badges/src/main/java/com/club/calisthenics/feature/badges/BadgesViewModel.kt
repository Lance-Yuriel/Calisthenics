package com.club.calisthenics.feature.badges

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.club.calisthenics.core.domain.model.Badge
import com.club.calisthenics.core.domain.model.UserBadge
import com.club.calisthenics.core.domain.repository.AuthRepository
import com.club.calisthenics.core.domain.repository.BadgeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class BadgesUiState(
    val isLoading: Boolean = true,
    val userBadges: List<UserBadge> = emptyList(),
    val availableBadges: List<Badge> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class BadgesViewModel @Inject constructor(
    private val badgeRepository: BadgeRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val userId = authRepository.currentUserId ?: ""

    val uiState: StateFlow<BadgesUiState> = combine(
        badgeRepository.getUserBadges(userId),
        badgeRepository.getActiveBadges()
    ) { userBadges, activeBadges ->
        // Map user badges with badge details from the catalog
        val enrichedUserBadges = userBadges.map { ub ->
            ub.copy(badge = activeBadges.find { it.id == ub.badgeId })
        }
        
        BadgesUiState(
            isLoading = false,
            userBadges = enrichedUserBadges,
            availableBadges = activeBadges
        )
    }.catch { e ->
        emit(BadgesUiState(isLoading = false, errorMessage = e.localizedMessage))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BadgesUiState()
    )
}
