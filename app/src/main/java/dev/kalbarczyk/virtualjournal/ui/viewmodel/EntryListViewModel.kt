package dev.kalbarczyk.virtualjournal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.kalbarczyk.virtualjournal.model.JournalEntry
import dev.kalbarczyk.virtualjournal.model.data.JournalEntryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntryListViewModel @Inject constructor(
    private val repository: JournalEntryRepository,
) : ViewModel() {
    var state = MutableStateFlow(listOf<JournalEntry>())

    fun load(){
        state.update { repository.entries }

        viewModelScope.launch {
            repository.initDb()
        }
    }
}