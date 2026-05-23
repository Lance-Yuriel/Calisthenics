package com.club.calisthenics.core.data.model

import com.club.calisthenics.core.domain.model.Skill
import com.google.firebase.firestore.PropertyName

data class SkillDto(
    @get:PropertyName("name") @set:PropertyName("name")
    var name: String = "",
    
    @get:PropertyName("category") @set:PropertyName("category")
    var category: String = "",
    
    @get:PropertyName("difficultyTag") @set:PropertyName("difficultyTag")
    var difficultyTag: String = "",
    
    @get:PropertyName("description") @set:PropertyName("description")
    var description: String = "",
    
    @get:PropertyName("imageUrl") @set:PropertyName("imageUrl")
    var imageUrl: String? = null,
    
    @get:PropertyName("videoUrl") @set:PropertyName("videoUrl")
    var videoUrl: String? = null,
    
    @get:PropertyName("order") @set:PropertyName("order")
    var order: Int = 0,
    
    @get:PropertyName("published") @set:PropertyName("published")
    var published: Boolean = false
) {
    fun toDomain(id: String): Skill {
        return Skill(
            id = id,
            name = name,
            category = category,
            difficultyTag = difficultyTag,
            description = description,
            imageUrl = imageUrl,
            videoUrl = videoUrl,
            order = order,
            published = published
        )
    }
}
