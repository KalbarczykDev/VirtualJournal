package dev.kalbarczyk.virtualjournal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.kalbarczyk.virtualjournal.model.PinUiState
import dev.kalbarczyk.virtualjournal.model.data.PinRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PinViewModel @Inject constructor(
    private val repository: PinRepository
) : ViewModel() {

    val state =  MutableStateFlow(PinUiState())

    fun load(){
        viewModelScope.launch {
            val isSet = repository.isPinSet()
            state.value = state.value.copy(isPinSet = isSet)
        }
    }

    fun submitPin(input: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (state.value.isPinSet) {
                if (repository.isPinCorrect(input)) {
                    state.value = state.value.copy(pinError = false)
                    onSuccess()
                } else {
                    state.value = state.value.copy(pinError = true)
                }
            } else {
                repository.savePin(input)
                state.value = PinUiState(isPinSet = true, pinError = false)
                onSuccess()
            }
        }
    }

    fun resetError() {
        state.value = state.value.copy(pinError = false)
    }
}
