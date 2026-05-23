package com.club.calisthenics.feature.skills

import com.club.calisthenics.core.domain.model.Skill

sealed interface SkillsUiState {
    object Loading : SkillsUiState
    data class Success(
        val skillsByCategory: Map<String, List<Skill>>
    ) : SkillsUiState
    data class Error(val message: String) : SkillsUiState
}
