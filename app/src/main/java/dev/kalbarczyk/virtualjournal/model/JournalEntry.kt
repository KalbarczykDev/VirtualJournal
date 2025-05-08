package dev.kalbarczyk.virtualjournal.model

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import dev.kalbarczyk.virtualjournal.model.data.db.entity.JournalEntryEntity

data class JournalEntry(
    val id: Int,
    val content: String,
    val cityName: String? = null,
    val photoPath: String? = null,
    val voiceRecordingPath: String? = null
) {
    fun toEntity(): JournalEntryEntity {
        return JournalEntryEntity(
            id = id,
            content = content,
            cityName = cityName,
            photoPath = photoPath,
            voiceRecordingPath = voiceRecordingPath
        )
    }
}


val previewData = List(10) {
    JournalEntry(
        id = it,
        content = LoremIpsum(10).values.joinToString(),
        cityName = "Warszawa",
        photoPath = null,
        voiceRecordingPath = null
    )
}
