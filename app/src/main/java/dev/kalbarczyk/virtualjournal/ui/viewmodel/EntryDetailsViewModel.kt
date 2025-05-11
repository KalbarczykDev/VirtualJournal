package dev.kalbarczyk.virtualjournal.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.kalbarczyk.virtualjournal.model.JournalEntry
import dev.kalbarczyk.virtualjournal.model.data.JournalEntryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntryDetailsViewModel @Inject constructor(
    private val repository: JournalEntryRepository
) : ViewModel() {


    val state = MutableStateFlow<JournalEntry?>(null)


    fun load(id: Int) {
        viewModelScope.launch {
            state.value = repository.getById(id)
        }
    }


}