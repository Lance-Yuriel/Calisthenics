package com.club.calisthenics.core.data.repository

import com.club.calisthenics.core.domain.model.Skill
import com.club.calisthenics.core.domain.repository.SkillRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MockSkillRepository @Inject constructor() : SkillRepository {
    private val skills = MutableStateFlow(listOf(
        Skill("s1", "Standard Pull-up", "Pulling", "Beginner", "Foundational movement.", null, null, 1, true),
        Skill("s2", "Muscle Up", "Pulling", "Advanced", "The classic transition.", null, null, 2, true),
        Skill("s3", "Tuck Planche", "Pushing", "Intermediate", "Start of straight arm strength.", null, null, 3, true),
        Skill("s4", "Full Planche", "Pushing", "Elite", "Maximum pushing strength.", null, null, 4, true),
        Skill("s5", "Front Lever", "Pulling", "Advanced", "Horizontal pulling hold.", null, null, 5, true)
    ))

    override fun getPublishedSkills(): Flow<List<Skill>> = skills.map { list ->
        list.filter { it.published }.sortedBy { it.order }
    }

    override fun getAllSkills(): Flow<List<Skill>> = skills

    override suspend fun getSkillById(id: String): Skill? = skills.value.find { it.id == id }

    override suspend fun upsertSkill(skill: Skill) {
        skills.value = skills.value.filter { it.id != skill.id } + skill
    }

    override suspend fun deleteSkill(id: String) {
        skills.value = skills.value.filter { it.id != id }
    }
}
