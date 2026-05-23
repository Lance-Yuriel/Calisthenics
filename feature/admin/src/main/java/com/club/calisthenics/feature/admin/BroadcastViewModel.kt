package com.club.calisthenics.feature.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.club.calisthenics.core.domain.model.Announcement
import com.club.calisthenics.core.domain.model.AnnouncementType
import com.club.calisthenics.core.domain.repository.AnnouncementRepository
import com.club.calisthenics.core.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BroadcastViewModel @Inject constructor(
    private val announcementRepository: AnnouncementRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _saved = MutableStateFlow(false)
    val saved = _saved.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun sendBroadcast(title: String, body: String, type: AnnouncementType) {
        val authorId = authRepository.currentUserId ?: return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val announcement = Announcement(
                    id = "",
                    title = title,
                    body = body,
                    type = type,
                    createdAt = LocalDateTime.now(),
                    authorId = authorId
                )
                announcementRepository.createAnnouncement(announcement)
                _saved.value = true
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
