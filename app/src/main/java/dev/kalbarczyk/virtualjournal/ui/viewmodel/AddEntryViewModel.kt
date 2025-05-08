package dev.kalbarczyk.virtualjournal.ui.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.kalbarczyk.virtualjournal.model.JournalEntry
import dev.kalbarczyk.virtualjournal.model.data.JournalEntryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddEntryViewModel @Inject constructor(
    private val repository: JournalEntryRepository
): ViewModel() {
   // private val state = MutableStateFlow<List<JournalEntry>>(emptyList())




   fun load() {
        viewModelScope.launch {
         //   val allEntries = repository.getAll()
          //  state.value = allEntries
        }
    }

   fun addEntry(entry: JournalEntry) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                repository.add(entry)
            }
            load()
        }
    }
}