package com.club.calisthenics.feature.home

import com.club.calisthenics.core.domain.model.Event
import com.club.calisthenics.core.domain.model.User
import com.club.calisthenics.core.domain.model.UserStats

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(
        val user: User?,
        val featuredEvent: Event?,
        val stats: UserStats,
        val announcement: String? = null
    ) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
