package com.club.calisthenics.core.domain.model

data class Skill(
    val id: String,
    val name: String,
    val category: String,
    val difficultyTag: String,
    val description: String,
    val imageUrl: String?,
    val videoUrl: String?,
    val order: Int,
    val published: Boolean
)
