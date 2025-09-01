package mohsen.helinote.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import mohsen.helinote.ui.add_edit_note.AddEditScreenRoute
import mohsen.helinote.ui.notes.NotesScreen

@Composable
fun AppNavHost() {
    Surface(color = MaterialTheme.colorScheme.background) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = NavScreen.NotesScreen.route
        ) {
            composable(route = NavScreen.NotesScreen.route) {
                NotesScreen(navController = navController, LocalContext.current)

            }

            composable(
                route = NavScreen.AddEditNoteScreen.route + "?noteId={noteId}&noteColor={noteColor}",
                arguments = listOf(
                    navArgument(name = "noteId") {
                        type = NavType.IntType
                        defaultValue = -1
                    },

                    navArgument(name = "noteColor") {
                        type = NavType.IntType
                        defaultValue = -1
                    },
                )
            ) {
                val color = it.arguments?.getInt("noteColor") ?: -1
                val editId = it.arguments?.getInt("noteId")
                AddEditScreenRoute(navController = navController, noteColor = color , editId = editId)
            }

        }
    }

}