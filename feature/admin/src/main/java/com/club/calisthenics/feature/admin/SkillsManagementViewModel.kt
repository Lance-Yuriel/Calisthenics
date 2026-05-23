package com.club.calisthenics.feature.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.club.calisthenics.core.domain.model.Skill
import com.club.calisthenics.core.domain.repository.SkillRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SkillsManagementViewModel @Inject constructor(
    private val skillRepository: SkillRepository
) : ViewModel() {

    val allSkills: StateFlow<List<Skill>> = skillRepository.getAllSkills()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteSkill(id: String) {
        viewModelScope.launch {
            skillRepository.deleteSkill(id)
        }
    }
}
