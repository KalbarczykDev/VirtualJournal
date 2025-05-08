package dev.kalbarczyk.virtualjournal.model.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import dev.kalbarczyk.virtualjournal.model.data.db.entity.JournalEntryEntity

@Dao
interface JournalEntryDao {

    @Insert
    suspend fun add(entry: JournalEntryEntity)

    @Update
    suspend fun update(entry: JournalEntryEntity)

    @Transaction
    @Query("SELECT * FROM journal_entry WHERE id = :id")
    suspend fun getById(id: Int): JournalEntryEntity?

    @Transaction
    @Query("SELECT * FROM journal_entry")
    suspend fun getAll(): List<JournalEntryEntity>
}