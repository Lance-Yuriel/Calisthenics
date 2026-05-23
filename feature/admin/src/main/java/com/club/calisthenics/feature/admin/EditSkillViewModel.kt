package com.club.calisthenics.feature.admin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.club.calisthenics.core.domain.model.Skill
import com.club.calisthenics.core.domain.repository.SkillRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditSkillViewModel @Inject constructor(
    private val skillRepository: SkillRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val skillId: String? = savedStateHandle["skillId"]

    private val _skillToEdit = MutableStateFlow<Skill?>(null)
    val skillToEdit = _skillToEdit.asStateFlow()

    private val _saved = MutableStateFlow(false)
    val saved = _saved.asStateFlow()

    init {
        skillId?.let { id ->
            viewModelScope.launch {
                _skillToEdit.value = skillRepository.getSkillById(id)
            }
        }
    }

    fun saveSkill(skill: Skill) {
        viewModelScope.launch {
            skillRepository.upsertSkill(skill)
            _saved.value = true
        }
    }
}
