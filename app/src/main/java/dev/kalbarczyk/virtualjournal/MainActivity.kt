package dev.kalbarczyk.virtualjournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.kalbarczyk.virtualjournal.model.JournalEntry
import dev.kalbarczyk.virtualjournal.ui.theme.VirtualJournalTheme
import dev.kalbarczyk.virtualjournal.ui.view.EntryListScreen
import dev.kalbarczyk.virtualjournal.ui.viewmodel.AddEntryViewModel
import dev.kalbarczyk.virtualjournal.ui.viewmodel.EntryListViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val entryListViewModel: EntryListViewModel by viewModels()
    val addEntryViewModel: AddEntryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VirtualJournalTheme {
                val navController = rememberNavController()

                NavHost(
                    navController,
                    Destinations.LIST_ENTRIES_DESTINATION
                ) {
                    // Entry list
                    composable(Destinations.LIST_ENTRIES_DESTINATION) {
                        val vm: EntryListViewModel = entryListViewModel

                        val avm: AddEntryViewModel = addEntryViewModel

                        LaunchedEffect(Unit) {
                            vm.load()
                        }


                        val state by vm.state.collectAsStateWithLifecycle()

                        EntryListScreen(
                            entries = state,
                            onAddClicked = {
                                //  navController.navigate(Destinations.ADD_ENTRY_DESTINATION)

                                val newEntry = JournalEntry(
                                    id = 0,
                                    content = "Note from user",
                                    cityName = "Warszawa",
                                    photoPath = null,
                                    voiceRecordingPath = null
                                )
                                avm.addEntry(newEntry)
                                vm.load()
                            },
                            {}
                        )

                    }
                    // Add entry
                    composable(Destinations.ADD_ENTRY_DESTINATION) {

                    }
                }
            }
        }
    }
}

object Destinations {
    const val ARG_ID = "id"
    const val LIST_ENTRIES_DESTINATION = "entry_list"
    const val ADD_ENTRY_DESTINATION = "add_entry"
    const val ENTRY_DETAILS_DESTINATION = "details/{$ARG_ID}"

    fun getRouteForDetails(id: Int): String {
        return ENTRY_DETAILS_DESTINATION.replace("{$ARG_ID}", id.toString())
    }
}