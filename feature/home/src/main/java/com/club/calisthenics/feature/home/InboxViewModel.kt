package com.club.calisthenics.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.club.calisthenics.core.domain.model.Announcement
import com.club.calisthenics.core.domain.repository.AnnouncementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class InboxViewModel @Inject constructor(
    private val announcementRepository: AnnouncementRepository
) : ViewModel() {

    val announcements: StateFlow<List<Announcement>> = announcementRepository.getAnnouncements()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
