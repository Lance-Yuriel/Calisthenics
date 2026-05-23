package com.club.calisthenics.core.domain.repository

import com.club.calisthenics.core.domain.model.Skill
import kotlinx.coroutines.flow.Flow

interface SkillRepository {
    fun getPublishedSkills(): Flow<List<Skill>>
    fun getAllSkills(): Flow<List<Skill>>
    suspend fun getSkillById(id: String): Skill?
    suspend fun upsertSkill(skill: Skill)
    suspend fun deleteSkill(id: String)
}
