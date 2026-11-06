package com.rojas.consumodatos.di

import com.rojas.consumodatos.data.repository.ContactRepositoryImpl
import com.rojas.consumodatos.domain.repository.ContactRepository
import com.rojas.consumodatos.data.repository.TaskRepositoryImpl
import com.rojas.consumodatos.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindContactRepository(
        impl: ContactRepositoryImpl
    ): ContactRepository
    @Binds
    @Singleton
    fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository
}
