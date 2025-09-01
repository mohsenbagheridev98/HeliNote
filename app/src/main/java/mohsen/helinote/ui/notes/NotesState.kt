package mohsen.helinote.ui.notes

import mohsen.helinote.domain.model.Note
import mohsen.helinote.domain.utils.NoteOrder
import mohsen.helinote.domain.utils.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
