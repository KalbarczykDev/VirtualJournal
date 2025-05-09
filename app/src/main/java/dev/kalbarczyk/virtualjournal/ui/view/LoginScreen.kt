package dev.kalbarczyk.virtualjournal.ui.view

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

@Composable
fun PinLoginScreen(onSuccess: () -> Unit) {
    val context = LocalContext.current
    var pin by rememberSaveable { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter PIN")
        OutlinedTextField(
            value = pin,
            onValueChange = {
                pin = it
                error = false
            },
            label = { Text("PIN") },
            isError = error,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (isPinCorrect(context, pin)) {
                onSuccess()
            } else {
                error = true
            }
        }) {
            Text("Unlock")
        }

        if (error) {
            Text("Incorrect PIN", color = MaterialTheme.colorScheme.error)
        }
    }
}

fun savePin(context: Context, pin: String) {
    getEncryptedPrefs(context).edit().putString("user_pin", pin).apply()
}

fun isPinCorrect(context: Context, enteredPin: String): Boolean {
    val storedPin = getEncryptedPrefs(context).getString("user_pin", null)
    return storedPin == enteredPin
}

fun getEncryptedPrefs(context: Context): SharedPreferences {
    val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    return EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}

@Preview(showBackground = true)
@Composable
fun PinLoginScreenPreview() {
    PinLoginScreen(onSuccess = {})
}
