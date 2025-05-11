package dev.kalbarczyk.virtualjournal.ui.view

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Stop
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
import dev.kalbarczyk.virtualjournal.model.previewData
import dev.kalbarczyk.virtualjournal.utils.audio.AndroidAudioRecorder
import java.io.File


@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Suppress("MissingPermission")
@Composable
fun EditEntryScreen(
    state: JournalEntry,
    onSave: (JournalEntry) -> Unit,
    onBack: () -> Unit,
    recorder: AndroidAudioRecorder? = null,
) {
    val context = LocalContext.current
    val isInPreview = LocalInspectionMode.current

    var content by rememberSaveable { mutableStateOf(state.content) }
    val cityName = state.cityName ?: "Unknown"
    var audioPath by rememberSaveable { mutableStateOf(state.voiceRecordingPath) }

    val audioPermissionState = if (!isInPreview) {
        rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    } else null

    var isRecording by remember { mutableStateOf(false) }
    val cacheDir = context.cacheDir

    LaunchedEffect(audioPermissionState?.status) {
        if (audioPermissionState != null && !audioPermissionState.status.isGranted) {
            audioPermissionState.launchPermissionRequest()
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
                            onSave(
                                state.copy(
                                    content = content,
                                    voiceRecordingPath = audioPath
                                )
                            )
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

            Row {
                IconButton(onClick = {
                    if (audioPermissionState?.status?.isGranted != true) {
                        audioPermissionState?.launchPermissionRequest()
                        return@IconButton
                    }

                    if (!isRecording) {
                        File(cacheDir, "audio_${System.currentTimeMillis()}.mp4").also {
                            recorder?.start(it)
                            audioPath = it.absolutePath
                        }
                    } else {
                        recorder?.stop()
                    }
                    isRecording = !isRecording
                }) {
                    Icon(
                        imageVector = if (isRecording) Icons.Filled.Stop else Icons.Filled.Mic,
                        contentDescription = null
                    )
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Your note") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EditEntryScreenPreview() {
    EditEntryScreen(
        state = previewData[0],
        onSave = {},
        onBack = {},
        recorder = null
    )
}