package dev.kalbarczyk.virtualjournal.model.data

import dev.kalbarczyk.virtualjournal.model.JournalEntry

interface JournalEntryRepository {

    val entries: List<JournalEntry>

    suspend fun initDb()

    suspend fun add(entry: JournalEntry)

    suspend fun update(entry: JournalEntry)

    suspend fun getById(id: Int): JournalEntry?

    suspend fun getAll(): List<JournalEntry>


}