package com.club.calisthenics.feature.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.club.calisthenics.core.domain.model.User
import com.club.calisthenics.core.domain.repository.AuthRepository
import com.club.calisthenics.core.domain.repository.StorageRepository
import com.club.calisthenics.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val storageRepository: StorageRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    val user: StateFlow<User?> = authRepository.isUserLoggedIn.flatMapLatest { isLoggedIn ->
        if (isLoggedIn) {
            val userId = authRepository.currentUserId ?: ""
            Log.d("ProfileViewModel", "User is logged in. Fetching data for UID: $userId")
            userRepository.getCurrentUser(userId)
        } else {
            Log.d("ProfileViewModel", "User is NOT logged in.")
            flowOf(null)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun updateProfileImage(uri: android.net.Uri) {
        val uid = authRepository.currentUserId ?: return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val path = "profiles/$uid.jpg"
                val result = storageRepository.uploadImage(uri, path)
                result.onSuccess { url ->
                    userRepository.updateProfile(uid, null, url)
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error uploading image", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateProfileBitmap(bitmap: android.graphics.Bitmap) {
        val uid = authRepository.currentUserId ?: return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val path = "profiles/$uid.jpg"
                val result = storageRepository.uploadBitmap(bitmap, path)
                result.onSuccess { url ->
                    userRepository.updateProfile(uid, null, url)
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error uploading bitmap", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }
}
