package dev.kalbarczyk.virtualjournal.model.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.kalbarczyk.virtualjournal.model.JournalEntry

@Entity(tableName = "journal_entry")
data class JournalEntryEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val content: String,
){
    fun toDomain(): JournalEntry {
        return JournalEntry(
            id = id,
            content = content
        )
    }
}