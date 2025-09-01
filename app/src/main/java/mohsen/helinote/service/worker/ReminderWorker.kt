package mohsen.helinote.service.worker

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import mohsen.helinote.domain.model.Note
import mohsen.helinote.domain.usecases.NoteUseCases
import mohsen.helinote.ui.MainActivity


@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted val  appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val noteUseCases: NoteUseCases
) :  CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val note = noteUseCases.getIndividualNote(inputData.getInt("id" , -1))
        if (note != null) {
        showNotification(appContext , note)
        }
       return Result.success()
    }
}

fun showNotification(
    context: Context,
    note: Note
) {

    val pendingIntent =
        PendingIntent.getActivity(context, 0, Intent(context , MainActivity::class.java),
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
    val CHANNEL_ID = "reminder" // The id of the channel.
    val notificationBuilder: NotificationCompat.Builder =
        NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(note.title)
            .setContentText(note.content)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_notification_overlay)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name: CharSequence = "Reminder" // The user-visible name of the channel.
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        notificationManager.createNotificationChannel(mChannel)
    }
    notificationManager.notify(
        note.id ?: 0,
        notificationBuilder.build()
    ) // 0 is the request code, it should be unique id

}