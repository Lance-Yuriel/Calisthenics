package com.club.calisthenics.feature.admin

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.club.calisthenics.core.domain.model.Event
import com.club.calisthenics.core.domain.repository.EventRepository
import com.club.calisthenics.core.domain.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EditEventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val storageRepository: StorageRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val eventId: String? = savedStateHandle["eventId"]

    private val _eventToEdit = MutableStateFlow<Event?>(null)
    val eventToEdit = _eventToEdit.asStateFlow()

    private val _saved = MutableStateFlow(false)
    val saved = _saved.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        eventId?.let { id ->
            viewModelScope.launch {
                _eventToEdit.value = eventRepository.getEventById(id).firstOrNull()
            }
        }
    }

    fun saveEvent(
        title: String,
        description: String,
        location: String,
        dateTime: LocalDateTime,
        capacity: Int,
        imageUri: Uri?,
        imageBitmap: android.graphics.Bitmap? = null
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentEvent = _eventToEdit.value
                var imageUrl = currentEvent?.coverImageUrl

                if (imageBitmap != null) {
                    val path = "events/${UUID.randomUUID()}.jpg"
                    val uploadResult = storageRepository.uploadBitmap(imageBitmap, path)
                    imageUrl = uploadResult.getOrNull() ?: imageUrl
                } else if (imageUri != null) {
                    val path = "events/${UUID.randomUUID()}.jpg"
                    android.util.Log.d("EditEventVM", "Uploading image to: $path")
                    val uploadResult = storageRepository.uploadImage(imageUri, path)
                    imageUrl = uploadResult.getOrNull() ?: imageUrl
                    android.util.Log.d("EditEventVM", "Upload result: $imageUrl")
                }

                if (currentEvent != null) {
                    val updatedEvent = currentEvent.copy(
                        title = title,
                        description = description,
                        location = location,
                        startAt = dateTime,
                        endAt = dateTime.plusHours(2),
                        capacity = capacity,
                        coverImageUrl = imageUrl
                    )
                    eventRepository.updateEvent(updatedEvent)
                } else {
                    val newEvent = Event(
                        id = "",
                        title = title,
                        description = description,
                        location = location,
                        locationUrl = null,
                        startAt = dateTime,
                        endAt = dateTime.plusHours(2),
                        capacity = capacity,
                        coverImageUrl = imageUrl,
                        state = com.club.calisthenics.core.domain.model.EventState.PUBLISHED,
                        qrPayload = UUID.randomUUID().toString()
                    )
                    eventRepository.createEvent(newEvent)
                }
                _saved.value = true
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
