package com.club.calisthenics.feature.admin

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.club.calisthenics.core.domain.model.Event
import com.club.calisthenics.core.domain.model.EventState
import com.club.calisthenics.core.domain.repository.EventRepository
import com.club.calisthenics.core.domain.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val storageRepository: StorageRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _eventCreated = MutableStateFlow(false)
    val eventCreated = _eventCreated.asStateFlow()

    fun createEvent(
        title: String,
        description: String,
        location: String,
        dateTime: LocalDateTime,
        capacity: Int,
        imageUri: Uri?
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                var imageUrl: String? = null
                if (imageUri != null) {
                    val path = "events/${UUID.randomUUID()}.jpg"
                    val uploadResult = storageRepository.uploadImage(imageUri, path)
                    imageUrl = uploadResult.getOrNull()
                }

                val newEvent = Event(
                    id = "", // Firestore will generate this
                    title = title,
                    description = description,
                    location = location,
                    locationUrl = null,
                    startAt = dateTime,
                    endAt = dateTime.plusHours(2), // Default 2h duration
                    capacity = capacity,
                    coverImageUrl = imageUrl,
                    state = EventState.PUBLISHED,
                    qrPayload = UUID.randomUUID().toString()
                )
                eventRepository.createEvent(newEvent)
                _eventCreated.value = true
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
