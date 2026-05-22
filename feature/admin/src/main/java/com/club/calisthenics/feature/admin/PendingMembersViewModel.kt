package com.club.calisthenics.feature.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.club.calisthenics.core.domain.model.MemberStatus
import com.club.calisthenics.core.domain.model.User
import com.club.calisthenics.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PendingMembersViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val pendingUsers: StateFlow<List<User>> = userRepository.getPendingUsers()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun approveMember(userId: String) {
        viewModelScope.launch {
            userRepository.updateMemberStatus(userId, MemberStatus.APPROVED)
        }
    }

    fun rejectMember(userId: String) {
        viewModelScope.launch {
            userRepository.updateMemberStatus(userId, MemberStatus.REJECTED)
        }
    }
}
