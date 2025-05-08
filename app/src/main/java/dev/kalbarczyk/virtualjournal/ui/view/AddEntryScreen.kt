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
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
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
import com.google.android.gms.location.LocationServices
import dev.kalbarczyk.virtualjournal.R
import dev.kalbarczyk.virtualjournal.model.JournalEntry
import dev.kalbarczyk.virtualjournal.utils.LocationManager
import dev.kalbarczyk.virtualjournal.utils.audio.AndroidAudioRecorder
import java.io.File


@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Suppress("MissingPermission")
@Composable
fun AddEntryScreen(
    onSave: (JournalEntry) -> Unit,
    onBack: () -> Unit,
    recorder: AndroidAudioRecorder? = null,
    player: AndroidAudioRecorder? = null,
) {

    val context = LocalContext.current

    val isInPreview = LocalInspectionMode.current

    var content by rememberSaveable { mutableStateOf("") }
    var cityName by rememberSaveable { mutableStateOf(if (isInPreview) "Preview City" else "Locating...") }
    var photoUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var audioPath by rememberSaveable { mutableStateOf<String?>(null) }

    val locationPermissionState = if (!isInPreview) {
        rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    } else null

    val audioPermissionState = if (!isInPreview) {
        rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    } else null


    val cacheDir = context.cacheDir
    var audioFile by remember { mutableStateOf<File?>(null) }

    val locationManager = remember {
        LocationManager(
            context = context,
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
                context
            )
        )
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { photoUri = it },
    )

    if (!isInPreview) {
        LaunchedEffect(locationPermissionState?.status) {
            if (locationPermissionState?.status?.isGranted == true) {
                cityName = locationManager.getCityName() ?: "Unknown"
            } else {
                locationPermissionState?.launchPermissionRequest()
            }

            if (audioPermissionState != null && !audioPermissionState.status.isGranted) {
                audioPermissionState.launchPermissionRequest()
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


            /*   Column(
                   verticalArrangement = Arrangement.spacedBy(8.dp),
                   horizontalAlignment = Alignment.CenterHorizontally
               ) {
                   Button(onClick = {
                       File(cacheDir, "audio.mp3").also {
                           recorder?.start(it)
                           audioFile = it
                       }
                   }) {
                       Text("Start recording")
                   }

                   Button(onClick = {
                       recorder?.stop()
                   }) {
                       Text("Stop recording")
                   }

                   Button(onClick = {
                       audioFile?.let { player?.start(it) }
                   }) {
                       Text("Play")
                   }

                   Button(onClick = {
                       player?.stop()
                   }) {
                       Text("Stop playing")
                   }
               }*/

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