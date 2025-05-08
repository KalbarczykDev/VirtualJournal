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
import dev.kalbarczyk.virtualjournal.ui.theme.VirtualJournalTheme
import dev.kalbarczyk.virtualjournal.ui.view.AddEntryScreen
import dev.kalbarczyk.virtualjournal.ui.view.EntryListScreen
import dev.kalbarczyk.virtualjournal.ui.viewmodel.AddEntryViewModel
import dev.kalbarczyk.virtualjournal.ui.viewmodel.EntryListViewModel
import dev.kalbarczyk.virtualjournal.utils.audio.AndroidAudioPlayer
import dev.kalbarczyk.virtualjournal.utils.audio.AndroidAudioRecorder
import java.io.File

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val entryListViewModel: EntryListViewModel by viewModels()
    private val addEntryViewModel: AddEntryViewModel by viewModels()

    private val recorder by lazy {
        AndroidAudioRecorder(applicationContext)
    }
    private val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }

    private var audioFile: File? = null

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



                        LaunchedEffect(Unit) {
                            vm.load()
                        }


                        val state by vm.state.collectAsStateWithLifecycle()

                        EntryListScreen(
                            entries = state,
                            onAddClicked = {
                                navController.navigate(Destinations.ADD_ENTRY_DESTINATION)
                            },
                            {}
                        )

                    }
                    // Add entry
                    composable(Destinations.ADD_ENTRY_DESTINATION) {
                        val vm: AddEntryViewModel = addEntryViewModel
                        LaunchedEffect(Unit) {
                            vm.load()
                        }



                        AddEntryScreen(
                            onSave = { entry ->
                                addEntryViewModel.addEntry(entry)
                                navController.popBackStack()
                            },
                            onBack = {
                                navController.popBackStack()
                            }
                        )
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