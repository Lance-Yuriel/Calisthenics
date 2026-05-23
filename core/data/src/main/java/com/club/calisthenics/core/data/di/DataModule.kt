package com.club.calisthenics.core.data.di

import com.club.calisthenics.core.data.repository.*
import com.club.calisthenics.core.domain.repository.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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
    abstract fun bindUserRepository(repo: FirestoreUserRepository): UserRepository

    @Binds
    @Singleton
    abstract fun bindEventRepository(repo: FirestoreEventRepository): EventRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(repo: FirebaseAuthRepository): AuthRepository

    @Binds
    @Singleton
    abstract fun bindSkillRepository(repo: FirestoreSkillRepository): SkillRepository

    @Binds
    @Singleton
    abstract fun bindStorageRepository(repo: FirebaseStorageRepository): StorageRepository

    @Binds
    @Singleton
    abstract fun bindBadgeRepository(repo: FirestoreBadgeRepository): BadgeRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

        @Provides
        @Singleton
        fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()
    }
}
