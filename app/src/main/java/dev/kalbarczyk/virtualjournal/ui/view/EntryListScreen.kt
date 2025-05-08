package dev.kalbarczyk.virtualjournal.ui.view

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import dev.kalbarczyk.virtualjournal.model.JournalEntry
import dev.kalbarczyk.virtualjournal.model.previewData
import dev.kalbarczyk.virtualjournal.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryListScreen(entries: List<JournalEntry>) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {}
            )
        }
    ){ innerPaddings ->

        val systemPaddings = WindowInsets.systemBars.asPaddingValues()

        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 8.dp)
                .padding(bottom = systemPaddings.calculateBottomPadding()),
            contentPadding = innerPaddings,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(entries, key = { it.id }) {
                EntryItemView(it)
            }
        }

    }
}

@Composable
fun EntryItemView(entry: JournalEntry) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { /* TODO */ })
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = entry.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            entry.cityName?.let { city ->
                Text(
                    text = city,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                     fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                     maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EntryListScreenPreview() {
    EntryListScreen(entries = previewData)
}