package dev.kalbarczyk.virtualjournal.ui.view

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import dev.kalbarczyk.virtualjournal.R
import dev.kalbarczyk.virtualjournal.model.JournalEntry
import dev.kalbarczyk.virtualjournal.model.previewData
import dev.kalbarczyk.virtualjournal.utils.audio.AndroidAudioRecorder


@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Suppress("MissingPermission")
@Composable
fun EntryDetailsScreen(
    state: JournalEntry,
    onBack: () -> Unit,
    player: AndroidAudioRecorder? = null,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.entry_details_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
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
                        append(state.cityName)
                    }
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = state.content,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            AsyncImage(
                modifier = Modifier.fillMaxWidth().height(240.dp).clip(RoundedCornerShape(8.dp)),
                model = state.photoPath, contentDescription = null
            )
        }


    }
}


@Preview(showBackground = true)
@Composable
fun EntryDetailsScreenPreview() {
    EntryDetailsScreen(
        state = previewData[0],
        {},
        null,
    )
}