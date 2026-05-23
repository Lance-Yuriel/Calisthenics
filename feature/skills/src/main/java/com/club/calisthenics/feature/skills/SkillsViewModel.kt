package com.club.calisthenics.feature.skills

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.club.calisthenics.core.domain.repository.SkillRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SkillsViewModel @Inject constructor(
    private val skillRepository: SkillRepository
) : ViewModel() {

    val uiState: StateFlow<SkillsUiState> = skillRepository.getPublishedSkills()
        .map<List<com.club.calisthenics.core.domain.model.Skill>, SkillsUiState> { skills ->
            SkillsUiState.Success(
                skillsByCategory = skills.groupBy { it.category }
            )
        }
        .catch { e ->
            emit(SkillsUiState.Error(e.localizedMessage ?: "Unknown error"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SkillsUiState.Loading
        )
}
