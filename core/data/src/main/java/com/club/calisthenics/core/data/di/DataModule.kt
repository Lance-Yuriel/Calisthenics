package com.club.calisthenics.core.data.di

import com.club.calisthenics.core.data.repository.FirebaseAuthRepository
import com.club.calisthenics.core.data.repository.FirestoreEventRepository
import com.club.calisthenics.core.data.repository.FirestoreSkillRepository
import com.club.calisthenics.core.data.repository.FirestoreUserRepository
import com.club.calisthenics.core.domain.repository.AuthRepository
import com.club.calisthenics.core.domain.repository.EventRepository
import com.club.calisthenics.core.domain.repository.SkillRepository
import com.club.calisthenics.core.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        firestoreUserRepository: FirestoreUserRepository
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindEventRepository(
        firestoreEventRepository: FirestoreEventRepository
    ): EventRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        firebaseAuthRepository: FirebaseAuthRepository
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindSkillRepository(
        firestoreSkillRepository: FirestoreSkillRepository
    ): SkillRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
    }
}
