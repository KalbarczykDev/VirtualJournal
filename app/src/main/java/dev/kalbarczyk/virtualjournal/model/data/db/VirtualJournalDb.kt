package dev.kalbarczyk.virtualjournal.model.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.kalbarczyk.virtualjournal.model.data.db.dao.JournalEntryDao
import dev.kalbarczyk.virtualjournal.model.data.db.entity.JournalEntryEntity

@Database(entities = [JournalEntryEntity::class], version = 1)
abstract class VirtualJournalDb : RoomDatabase() {
    abstract val journal: JournalEntryDao
}