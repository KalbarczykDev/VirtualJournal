package dev.kalbarczyk.virtualjournal.ui.view

import android.Manifest
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dev.kalbarczyk.virtualjournal.model.JournalEntry
import dev.kalbarczyk.virtualjournal.utils.getCityFromLocation
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.kalbarczyk.virtualjournal.R

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Suppress("MissingPermission")
@Composable
fun AddEntryScreen(
    onSave: (JournalEntry) -> Unit,
    onBack: () -> Unit,
) {

    val context = LocalContext.current

    val isInPreview = LocalInspectionMode.current

    var content by rememberSaveable { mutableStateOf("") }
    var cityName by rememberSaveable { mutableStateOf(if (isInPreview) "Preview City" else "Locating...") }
    // var photoUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    // var audioPath by rememberSaveable { mutableStateOf<String?>(null) }

    if (!isInPreview) {
        val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

        LaunchedEffect(locationPermissionState.status) {
            if (locationPermissionState.status.isGranted) {
                cityName = getCityFromLocation(context) ?: "Unknown"
            } else {
                locationPermissionState.launchPermissionRequest()
            }
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.add_button_description),
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onSave(JournalEntry(0, content, cityName, null, null))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Save,
                            contentDescription = stringResource(R.string.add_button_description),
                        )
                    }
                }
            )
        }
    ) { innerPadding ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            TextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Your note") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("City: $cityName")

            Spacer(modifier = Modifier.height(16.dp))



            Spacer(modifier = Modifier.height(24.dp))


        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddEntryScreenPreview() {
    AddEntryScreen(
        onSave = {},
        onBack = {},
    )
}