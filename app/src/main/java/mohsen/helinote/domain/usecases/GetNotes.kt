package mohsen.helinote.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mohsen.helinote.domain.model.Note
import mohsen.helinote.domain.repository.NoteRepository
import mohsen.helinote.domain.utils.NoteOrder
import mohsen.helinote.domain.utils.OrderType

/*
  Use cases must have one public function for accessibility
  Usecases are good, since they can be used by different viewModels in our app. Hence, we have to write less
  repetitive code in viewModels.
 */
class GetNotes(
    private val repository: NoteRepository
) {
    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)
    ): Flow<List<Note>> {
        return repository.getNotes().map { notes ->
            //whatever the list we get from the repository, we map that to our newList
            when (noteOrder.orderType) {
                is OrderType.Ascending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> notes.sortedBy { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedBy { it.timestamp }
                        is NoteOrder.Color -> notes.sortedBy { it.color }

                    }
                }

                is OrderType.Descending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedByDescending { it.timestamp }
                        is NoteOrder.Color -> notes.sortedByDescending { it.color }

                    }
                }
            }
        }
    }
}