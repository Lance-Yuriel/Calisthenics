package com.club.calisthenics.core.data.repository

import com.club.calisthenics.core.data.model.SkillDto
import com.club.calisthenics.core.domain.model.Skill
import com.club.calisthenics.core.domain.repository.SkillRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreSkillRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : SkillRepository {

    override fun getPublishedSkills(): Flow<List<Skill>> = callbackFlow {
        val subscription = firestore.collection("skills")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val skills = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(SkillDto::class.java)?.toDomain(doc.id)
                } ?: emptyList()

                val filtered = skills
                    .filter { it.published }
                    .sortedBy { it.order }
                
                trySend(filtered)
            }
        awaitClose { subscription.remove() }
    }

    override fun getAllSkills(): Flow<List<Skill>> = callbackFlow {
        val subscription = firestore.collection("skills")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val skills = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(SkillDto::class.java)?.toDomain(doc.id)
                } ?: emptyList()
                
                trySend(skills.sortedBy { it.order })
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun getSkillById(id: String): Skill? {
        val snapshot = firestore.collection("skills").document(id).get().await()
        return snapshot.toObject(SkillDto::class.java)?.toDomain(snapshot.id)
    }

    override suspend fun upsertSkill(skill: Skill) {
        val dto = SkillDto(
            name = skill.name,
            category = skill.category,
            difficultyTag = skill.difficultyTag,
            description = skill.description,
            imageUrl = skill.imageUrl,
            videoUrl = skill.videoUrl,
            order = skill.order,
            published = skill.published
        )
        if (skill.id.isBlank()) {
            firestore.collection("skills").add(dto).await()
        } else {
            firestore.collection("skills").document(skill.id).set(dto).await()
        }
    }

    override suspend fun deleteSkill(id: String) {
        firestore.collection("skills").document(id).delete().await()
    }
}
