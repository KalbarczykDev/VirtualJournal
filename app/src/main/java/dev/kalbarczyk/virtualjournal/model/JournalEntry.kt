package dev.kalbarczyk.virtualjournal.model

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import dev.kalbarczyk.virtualjournal.model.data.db.entity.JournalEntryEntity

data class JournalEntry (
    val id: Int,
    val content: String,
){
    fun toEntity(): JournalEntryEntity {
        return JournalEntryEntity(
            id = id,
            content = content
        )
    }
}



val previewData = List(10){
    JournalEntry(it, LoremIpsum(10).values.joinToString(""))
}