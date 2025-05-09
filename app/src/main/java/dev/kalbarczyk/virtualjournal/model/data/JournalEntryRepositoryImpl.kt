package dev.kalbarczyk.virtualjournal.model.data

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import dev.kalbarczyk.virtualjournal.model.JournalEntry
import dev.kalbarczyk.virtualjournal.model.data.db.VirtualJournalDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class JournalEntryRepositoryImpl @Inject constructor(
    private val db: VirtualJournalDb
) : JournalEntryRepository {

    private val _entries: MutableList<JournalEntry>
    override val entries: List<JournalEntry>
        get() = _entries


    init {
        _entries = List(10) {
            JournalEntry(
                0, LoremIpsum(10).values.joinToString(),
                cityName = "Radom",
                photoPath = null,
                voiceRecordingPath = null
            )
        }.toMutableList()
    }

    override suspend fun initDb() = withContext(Dispatchers.IO) {
        if (db.journal.getAll().isEmpty()) {
            for (entry in _entries) {
                db.journal.add(entry.toEntity())
            }
        }
    }

    override suspend fun add(entry: JournalEntry) = withContext(Dispatchers.IO) {
        db.journal.add(entry.toEntity())
    }

    override suspend fun update(entry: JournalEntry) = withContext(Dispatchers.IO) {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: Int): JournalEntry? = withContext(Dispatchers.IO) {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): List<JournalEntry> = withContext(Dispatchers.IO) {
        db.journal.getAll().map { it.toDomain() }
    }
}