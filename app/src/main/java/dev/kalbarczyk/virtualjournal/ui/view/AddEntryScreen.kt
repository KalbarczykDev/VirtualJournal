package dev.kalbarczyk.virtualjournal.ui.view

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dev.kalbarczyk.virtualjournal.R
import dev.kalbarczyk.virtualjournal.model.JournalEntry
import dev.kalbarczyk.virtualjournal.utils.getCityFromLocation

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
    var photoUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var audioPath by rememberSaveable { mutableStateOf<String?>(null) }


    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { photoUri = it },
    )

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
                title = { Text(stringResource(R.string.add_new_entry_title)) },
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

                            val photoPath: String? = photoUri?.toString()
                            val voiceRecordingPath: String? = audioPath

                            onSave(JournalEntry(0, content, cityName, photoPath, voiceRecordingPath))
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

            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.location) + ": ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(cityName)
                    }
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                IconButton(onClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) {
                    Icon(
                        imageVector = Icons.Filled.PhotoCamera,
                        contentDescription = stringResource(R.string.add_button_description),
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.AddCircleOutline,
                        contentDescription = stringResource(R.string.add_button_description),
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Your note") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            AsyncImage(
                modifier = Modifier.fillMaxWidth().height(240.dp).clip(RoundedCornerShape(8.dp)),
                model = photoUri, contentDescription = null
            )


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