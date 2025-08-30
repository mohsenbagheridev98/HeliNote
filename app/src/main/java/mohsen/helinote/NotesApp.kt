package tdroid.note

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class NotesApp : Application() , Configuration.Provider  {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get()  {
            return Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build()

        }
}