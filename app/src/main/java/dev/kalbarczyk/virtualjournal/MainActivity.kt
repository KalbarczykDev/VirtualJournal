package dev.kalbarczyk.virtualjournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import dev.kalbarczyk.virtualjournal.ui.theme.VirtualJournalTheme
import dev.kalbarczyk.virtualjournal.ui.view.AddEntryScreen
import dev.kalbarczyk.virtualjournal.ui.view.EntryDetailsScreen
import dev.kalbarczyk.virtualjournal.ui.view.EntryListScreen
import dev.kalbarczyk.virtualjournal.ui.view.PinLoginScreen
import dev.kalbarczyk.virtualjournal.ui.viewmodel.AddEntryViewModel
import dev.kalbarczyk.virtualjournal.ui.viewmodel.EntryDetailsViewModel
import dev.kalbarczyk.virtualjournal.ui.viewmodel.EntryListViewModel
import dev.kalbarczyk.virtualjournal.ui.viewmodel.PinViewModel
import dev.kalbarczyk.virtualjournal.utils.audio.AndroidAudioPlayer
import dev.kalbarczyk.virtualjournal.utils.audio.AndroidAudioRecorder

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val entryListViewModel: EntryListViewModel by viewModels()
    private val addEntryViewModel: AddEntryViewModel by viewModels()
    private val entryDetailsViewModel: EntryDetailsViewModel by viewModels()
    private val pinViewModel: PinViewModel by viewModels()

    private val recorder by lazy {
        AndroidAudioRecorder(applicationContext)
    }
    private val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VirtualJournalTheme {
                val navController = rememberNavController()

                NavHost(
                    navController,
                    Destinations.LOGIN_DESTINATION
                ) {

                    //Login screen
                    composable(Destinations.LOGIN_DESTINATION) {

                        val vm: PinViewModel = pinViewModel

                        LaunchedEffect(Unit) {
                            vm.load()
                        }

                        val state by vm.state.collectAsStateWithLifecycle()

                        var pinInput by rememberSaveable { mutableStateOf("") }

                        PinLoginScreen(
                            pinUiState = state,
                            onUnlockButtonClicked = {
                                vm.submitPin(pinInput) {
                                    navController.navigate(Destinations.LIST_ENTRIES_DESTINATION) {
                                        popUpTo(Destinations.LOGIN_DESTINATION) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            },
                            pinInput = pinInput,
                            onPinInputChange = {
                                pinInput = it
                                vm.resetError()
                            },
                        )
                    }


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
                                navController.navigate(Destinations.ADD_ENTRY_DESTINATION) {
                                    launchSingleTop = true
                                }
                            },
                            onEntryClicked = { entry ->
                                navController.navigate(Destinations.getRouteForDetails(entry.id)) {
                                    launchSingleTop = true
                                }
                            }
                        )
                    }

                    composable(
                        Destinations.ENTRY_DETAILS_DESTINATION,
                        arguments = listOf(
                            navArgument(Destinations.ARG_ID) {
                                type = NavType.StringType
                            }
                        )) { backStackEntry ->

                        val vm: EntryDetailsViewModel = entryDetailsViewModel

                        val id = backStackEntry.arguments?.getString(Destinations.ARG_ID)?.toIntOrNull() ?: -1




                        LaunchedEffect(Unit) {
                            vm.load(id)
                        }

                        val state by vm.state.collectAsStateWithLifecycle()

                        if (state != null) {
                            EntryDetailsScreen(
                                state = state!!,
                                onBack = {
                                    navController.navigate(Destinations.LIST_ENTRIES_DESTINATION) {
                                        popUpTo(Destinations.LIST_ENTRIES_DESTINATION) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                },
                                player = player
                            )
                        }
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
                                navController.navigate(Destinations.LIST_ENTRIES_DESTINATION) {
                                    popUpTo(Destinations.LIST_ENTRIES_DESTINATION) { inclusive = true }
                                    launchSingleTop = true
                                }
                            },
                            onBack = {
                                navController.navigate(Destinations.LIST_ENTRIES_DESTINATION) {
                                    popUpTo(Destinations.LIST_ENTRIES_DESTINATION) { inclusive = true }
                                    launchSingleTop = true
                                }
                            },
                            recorder = recorder,
                        )

                    }
                }
            }
        }
    }
}

object Destinations {
    const val ARG_ID = "id"
    const val LOGIN_DESTINATION = "login"
    const val LIST_ENTRIES_DESTINATION = "entry_list"
    const val ADD_ENTRY_DESTINATION = "add_entry"
    const val ENTRY_DETAILS_DESTINATION = "details/{$ARG_ID}"

    fun getRouteForDetails(id: Int): String {
        return ENTRY_DETAILS_DESTINATION.replace("{$ARG_ID}", id.toString())
    }
}