package dev.kalbarczyk.virtualjournal.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.kalbarczyk.virtualjournal.model.data.JournalEntryRepository
import dev.kalbarczyk.virtualjournal.model.data.JournalEntryRepositoryImpl
import dev.kalbarczyk.virtualjournal.model.data.db.VirtualJournalDb
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindJournalEntryRepository(
        journalEntryRepositoryImpl: JournalEntryRepositoryImpl
    ): JournalEntryRepository

    companion object{
        @Provides
        @Singleton
        fun provideJournalDb(
            @ApplicationContext context: Context
        ): VirtualJournalDb{
            return Room.databaseBuilder(context, VirtualJournalDb::class.java, "virtual_journal.db")
                .fallbackToDestructiveMigration(true)
                .build()
        }
    }

}