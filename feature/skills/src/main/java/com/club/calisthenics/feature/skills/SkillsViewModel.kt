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

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val uiState: StateFlow<SkillsUiState> = combine(
        skillRepository.getPublishedSkills(),
        _searchQuery
    ) { skills, query ->
        val filteredSkills = if (query.isBlank()) {
            skills
        } else {
            skills.filter { 
                it.name.contains(query, ignoreCase = true) || 
                it.category.contains(query, ignoreCase = true) 
            }
        }
        
        SkillsUiState.Success(
            skillsByCategory = filteredSkills.groupBy { it.category }
        ) as SkillsUiState
    }
        .catch { e ->
            emit(SkillsUiState.Error(e.localizedMessage ?: "Unknown error"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SkillsUiState.Loading
        )

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }
}
