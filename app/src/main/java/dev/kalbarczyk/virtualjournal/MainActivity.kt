package dev.kalbarczyk.virtualjournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.kalbarczyk.virtualjournal.ui.theme.VirtualJournalTheme
import dev.kalbarczyk.virtualjournal.ui.viewmodel.ListViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val listViewModel: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VirtualJournalTheme {

                }
            }
        }
    }
