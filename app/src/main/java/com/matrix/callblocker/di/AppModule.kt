package com.matrix.callblocker.di

import android.app.Application
import androidx.room.Room
import com.matrix.callblocker.data.ContactDatabase
import com.matrix.callblocker.data.ContactRepositoryImpl
import com.matrix.callblocker.domain.repository.ContactRepository
import com.matrix.callblocker.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContactDatabase(app: Application): ContactDatabase {
        return Room.databaseBuilder(
            app,
            ContactDatabase::class.java,
            ContactDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideContactRepository(db: ContactDatabase): ContactRepository {
        return ContactRepositoryImpl(db.blockedContactDao)
    }

    @Provides
    @Singleton
    fun provideContactUseCases(repository: ContactRepository): ContactUseCases {
        return ContactUseCases(
            getContacts = GetContacts(repository),
            deleteContact = DeleteContact(repository),
            addContact = AddContact(repository),
            getContact = GetContact(repository),
            searchContact = SearchContact(repository)
        )
    }
}