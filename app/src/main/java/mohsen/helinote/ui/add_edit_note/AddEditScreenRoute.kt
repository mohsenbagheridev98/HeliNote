package mohsen.helinote.ui.add_edit_note

import android.Manifest
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.persistableBundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import mohsen.helinote.domain.model.Note
import mohsen.helinote.ui.add_edit_note.components.HintUI
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddEditScreenRoute(
    navController: NavController,
    noteColor: Int,
    editId : Int?
) {

    val viewModel: AddEditNoteViewModel = hiltViewModel()



    // Getting all the latest events
    LaunchedEffect(key1 = true) {
        if (editId != -1) {
            viewModel.getNote(editId!!)
        }
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                    SnackbarHostState().showSnackbar(
                        message = event.message
                    )
                }

                is AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }

                else -> {

                }
            }
        }
    }
    val notificationPermission = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS
    )


    AddEditScreen(
        notificationPermission = notificationPermission,
        noteColor = noteColor,
        noteTitle = viewModel.noteTitle.value,
        noteContent = viewModel.noteContent.value,
        reminderDate = viewModel.reminderDate.value,
        onEvent = {
            viewModel.onEvent(it)
        }

    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddEditScreen(
    notificationPermission: PermissionState?,
    noteColor: Int,
    onEvent: (AddEditNoteEvent) -> Unit,
    noteTitle: NoteTextFieldState,
    noteContent: NoteTextFieldState,
    reminderDate : Long
    ) {

    val noteBgAnimation = remember {
        Animatable(
            Color(
                if (noteColor != -1) noteColor else noteColor
            )
        )
    }

    var showDatePicker = remember { mutableStateOf(false) }
//    val selectedDate = datePickerState.selectedDateMillis?.let {
//        convertMillisToDate(it)
//    } ?: ""
    // To animate the above color, we need a scope
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            //fire an event to ViewModel to save a note, during onClick operation
            FloatingActionButton(
                onClick = {
                    onEvent(AddEditNoteEvent.SaveNote)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save Note")
            }
        },
        content = { padding ->
            //making a Row to select colors
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(noteBgAnimation.value)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    //make circles for each color we have
                    Note.noteColors.forEach { color ->
                        val colorInt = color.toArgb()

                        Box(
                            modifier = Modifier
                                .testTag("colorBox")
                                .size(50.dp)
                                .shadow(15.dp, CircleShape)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = 4.dp,
                                    color = if (noteColor == colorInt) {
                                        Color.Black  //color is selected
                                    } else {
                                        Color.Transparent  //color is deselected
                                    },
                                    shape = CircleShape
                                )
                                .clickable {
                                    scope.launch {
                                        noteBgAnimation.animateTo(
                                            targetValue = Color(colorInt),
                                            animationSpec = tween(durationMillis = 500)
                                        )
                                    }

                                    onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                                }

                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // For Title
                HintUI(
                    text = noteTitle.text,
                    hint = noteTitle.hint,
                    onValueChange = {
                        onEvent(AddEditNoteEvent.EnteredTitle(it))
                    },
                    onFocusChange = {
                        onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
                    },
                    isHintVisible = noteTitle.isHintVisible,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(16.dp))

                // For Content
                HintUI(
                    text = noteContent.text,
                    hint = noteContent.hint,
                    onValueChange = {
                        onEvent(AddEditNoteEvent.EnteredContent(it))
                    },
                    onFocusChange = {
                        onEvent(AddEditNoteEvent.ChangeContentFocus(it))
                    },
                    isHintVisible = noteContent.isHintVisible,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxHeight()
                )

                Text(text =if (reminderDate == 0L) "reminder" else convertMillisToDate(reminderDate) ,
                    color = Color.Gray,
                    modifier =
                    Modifier
                        .padding(top = 12.dp)
                        .clickable {
                            if (!notificationPermission?.status!!.isGranted) {
                                if (notificationPermission.status.shouldShowRationale) {
                                    // Show a rationale if needed (optional)
                                } else {
                                    // Request the permission
                                    notificationPermission.launchPermissionRequest()

                                }
                            }
                            showDatePicker.value = !showDatePicker.value
                        }
                )
            }

            if (showDatePicker.value) {
                DatePickerModal(onDateSelected = {
                    onEvent(AddEditNoteEvent.EnteredReminderDate(it ?: 0))
                } , onDismiss = {
                    showDatePicker.value = false
                })
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    var showTimePicker = remember {
        mutableStateOf(false)
    }

    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                if (!showTimePicker.value) {
                    showTimePicker.value = true
                    return@TextButton
                }

                val c = Calendar.getInstance(Locale.getDefault())
                c.time = Date(datePickerState.selectedDateMillis ?: 0)
                c.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                c.set(Calendar.MINUTE, timePickerState.minute)

                onDateSelected(c.time.time)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        if (showTimePicker.value == true) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally) {
                TimePicker(
                    state = timePickerState,
                )
            }

        } else {
            DatePicker(state = datePickerState)

        }

    }
}





fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.getDefault())
    return formatter.format(Date(millis))
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
@Preview
fun AddEditScreenPreview() {

    AddEditScreen(noteColor = 1 ,
        noteTitle = NoteTextFieldState(text = "test" , hint = "test" , false) ,
        noteContent = NoteTextFieldState(text = "test" , hint = "test" , false) ,
        onEvent = {},
        reminderDate = 0,
        notificationPermission = null
    )
}