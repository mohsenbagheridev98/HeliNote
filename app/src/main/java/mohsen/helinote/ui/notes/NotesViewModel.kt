package mohsen.helinote.ui.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import mohsen.helinote.domain.model.Note
import mohsen.helinote.domain.usecases.NoteUseCases
import mohsen.helinote.domain.utils.NoteOrder
import mohsen.helinote.domain.utils.OrderType
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {
    //We need to have one state wrapper class that represents the current UI state of the note screen
    //State will have 4 things : current note order, current list of notes, Restore notes and
    // order section visibility

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var recentDeletedNote: Note? = null

    /* We will get new instance of flow, when we call getNotes, so we have to cancel the old coroutine
       observing our database  */

    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.Order -> {
                //check whether the order is actually changed or not
                if (state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType) {
                    return
                }
                getNotes(event.noteOrder)
            }

            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                    recentDeletedNote = event.note
                }

            }

            is NotesEvent.RestoreNote -> {
                //keep reference of last deleted note
                viewModelScope.launch {
                    noteUseCases.addNote(recentDeletedNote ?: return@launch)

                    //null bcz if user call multiple times, same note can't be inserted again
                    recentDeletedNote = null
                }
            }

            is NotesEvent.ToggleOrderSection -> {
                _state.value =
                    state.value.copy(isOrderSectionVisible = !state.value.isOrderSectionVisible)
            }

            else -> {}
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotes(noteOrder).onEach { notes ->
            _state.value = state.value.copy(
                notes = notes,
                noteOrder = noteOrder
            )
        }.launchIn(viewModelScope)
    }
}