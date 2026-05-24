package com.club.calisthenics.core.data.di

import com.club.calisthenics.core.data.repository.*
import com.club.calisthenics.core.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MockDataModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(repo: MockUserRepository): UserRepository

    @Binds
    @Singleton
    abstract fun bindEventRepository(repo: MockEventRepository): EventRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(repo: MockAuthRepository): AuthRepository

    @Binds
    @Singleton
    abstract fun bindSkillRepository(repo: MockSkillRepository): SkillRepository

    @Binds
    @Singleton
    abstract fun bindStorageRepository(repo: MockStorageRepository): StorageRepository

    @Binds
    @Singleton
    abstract fun bindBadgeRepository(repo: MockBadgeRepository): BadgeRepository

    @Binds
    @Singleton
    abstract fun bindAnnouncementRepository(repo: MockAnnouncementRepository): AnnouncementRepository
}
