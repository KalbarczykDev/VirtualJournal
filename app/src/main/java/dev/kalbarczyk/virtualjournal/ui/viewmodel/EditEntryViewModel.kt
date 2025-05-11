package dev.kalbarczyk.virtualjournal.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.kalbarczyk.virtualjournal.model.JournalEntry
import dev.kalbarczyk.virtualjournal.model.data.JournalEntryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditEntryViewModel @Inject constructor(
    private val repository: JournalEntryRepository
) : ViewModel() {
    val state = MutableStateFlow<JournalEntry?>(null)


    fun load(id: Int) {
        viewModelScope.launch {
            state.value = repository.getById(id)
        }
    }

    fun update(entry: JournalEntry) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                repository.update(entry)
            }
        }
    }
}