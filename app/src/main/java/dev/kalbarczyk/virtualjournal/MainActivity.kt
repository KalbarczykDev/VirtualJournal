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
import dev.kalbarczyk.virtualjournal.ui.view.EntryListScreen
import dev.kalbarczyk.virtualjournal.ui.viewmodel.EntryListViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val entryListViewModel: EntryListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VirtualJournalTheme {
                    val navController = rememberNavController()

                NavHost(
                    navController,
                    Destinations.ENTRY_LIST_DESTINATION
                ){
                    composable(Destinations.ENTRY_LIST_DESTINATION) {
                        val vm: EntryListViewModel = entryListViewModel

                        LaunchedEffect(Unit) {
                            vm.load()
                        }


                        val state by vm.state.collectAsStateWithLifecycle()

                        EntryListScreen(
                            entries = state,
                        )

                    }
                }
                }
            }
        }
    }

object Destinations {
    const val ARG_ID = "id"
    const val ENTRY_LIST_DESTINATION = "entry_list"
    const val DETAILS_DESTINATION = "details/{$ARG_ID}"

    fun getRouteForDetails(id: Int): String {
        return DETAILS_DESTINATION.replace("{$ARG_ID}", id.toString())
    }
}