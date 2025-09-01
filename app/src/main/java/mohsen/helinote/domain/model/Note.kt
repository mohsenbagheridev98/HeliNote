package mohsen.helinote.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import mohsen.helinote.theme.BabyBlue
import mohsen.helinote.theme.LightGreen
import mohsen.helinote.theme.RedOrange
import mohsen.helinote.theme.RedPink
import mohsen.helinote.theme.Violet

@Entity
data class Note(
  val title : String,
  val content: String,
  val timestamp: Long,
  val timeReminder: Long,
  val color: Int,
  @PrimaryKey val id: Int? = null
){
    companion object{
      val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}

class InvalidNoteException(message:String): Exception(message)
