package mohsen.helinote.domain.usecases

import mohsen.helinote.domain.model.Note
import mohsen.helinote.domain.repository.NoteRepository


class GetNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }
}