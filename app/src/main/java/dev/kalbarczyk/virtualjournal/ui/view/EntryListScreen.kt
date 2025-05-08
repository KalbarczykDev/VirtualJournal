package dev.kalbarczyk.virtualjournal.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.kalbarczyk.virtualjournal.model.JournalEntry
import dev.kalbarczyk.virtualjournal.model.previewData


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryListScreen(entries: List<JournalEntry>) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Journal") },
                actions = {}
            )
        }
    ){ innerPaddings ->

        val systemPaddings = WindowInsets.systemBars.asPaddingValues()

        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 8.dp)
                .padding(bottom = innerPaddings.calculateBottomPadding()),
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
        modifier = Modifier.fillMaxWidth().clickable(onClick = { /* TODO */ } ),
    ){
        Row(modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = entry.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EntryListScreenPreview() {
    EntryListScreen(entries = previewData)
}