package com.club.calisthenics.feature.admin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.club.calisthenics.core.domain.model.Badge
import com.club.calisthenics.core.domain.repository.BadgeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditBadgeViewModel @Inject constructor(
    private val badgeRepository: BadgeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val badgeId: String? = savedStateHandle["badgeId"]

    private val _badgeToEdit = MutableStateFlow<Badge?>(null)
    val badgeToEdit = _badgeToEdit.asStateFlow()

    private val _saved = MutableStateFlow(false)
    val saved = _saved.asStateFlow()

    init {
        badgeId?.let { id ->
            viewModelScope.launch {
                _badgeToEdit.value = badgeRepository.getAllBadges().first().find { it.id == id }
            }
        }
    }

    fun saveBadge(badge: Badge) {
        viewModelScope.launch {
            if (badge.id.isBlank()) {
                badgeRepository.createBadge(badge)
            } else {
                badgeRepository.updateBadge(badge)
            }
            _saved.value = true
        }
    }
}
