package com.club.calisthenics.feature.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.club.calisthenics.core.domain.model.Badge
import com.club.calisthenics.core.domain.repository.BadgeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ManageBadgesViewModel @Inject constructor(
    private val badgeRepository: BadgeRepository
) : ViewModel() {

    val allBadges: StateFlow<List<Badge>> = badgeRepository.getAllBadges()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
