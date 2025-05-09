package dev.kalbarczyk.virtualjournal.model.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PinRepositoryImpl @Inject constructor(@ApplicationContext context: Context) : PinRepository {

    companion object {
        private const val PREFS_NAME = "secure_prefs"
        private const val PIN_KEY = "user_pin"
    }

    private val prefs: SharedPreferences

    init {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        prefs = EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override fun isPinSet(): Boolean = prefs.contains(PIN_KEY)

    override fun savePin(pin: String) {
        prefs.edit { putString(PIN_KEY, pin) }
    }

    override fun isPinCorrect(enteredPin: String): Boolean {
        val storedPin = prefs.getString(PIN_KEY, null)
        return storedPin == enteredPin
    }
}
