package dev.kalbarczyk.virtualjournal.model.data

interface PinRepository {
    fun isPinSet(): Boolean
    fun isPinCorrect(enteredPin: String): Boolean
    fun savePin(pin: String)
}
