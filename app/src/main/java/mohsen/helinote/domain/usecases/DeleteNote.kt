package mohsen.helinote.domain.usecases

import mohsen.helinote.domain.model.Note
import mohsen.helinote.domain.repository.NoteRepository

class DeleteNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}