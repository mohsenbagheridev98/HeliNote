package mohsen.helinote.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mohsen.helinote.data.data_source.NoteDB
import mohsen.helinote.data.repository.NoteRepositoryImpl
import mohsen.helinote.domain.repository.NoteRepository
import mohsen.helinote.domain.usecases.AddNote
import mohsen.helinote.domain.usecases.DeleteNote
import mohsen.helinote.domain.usecases.GetNote
import mohsen.helinote.domain.usecases.GetNotes
import mohsen.helinote.domain.usecases.NoteUseCases
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDB {
        return Room.databaseBuilder(
            app,
            NoteDB::class.java,
            NoteDB.DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDB  , workManager: WorkManager): NoteRepository {
        return NoteRepositoryImpl(db.noteDao , workManager )
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getIndividualNote = GetNote(repository)
        )
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }


}