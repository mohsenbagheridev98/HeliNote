# 📝 Note Reminder App

A simple and elegant Heli-Note Android application built using **Jetpack Compose**, with support for **reminders** and **local notifications**.

This project was built as part of a technical interview assessment. It showcases best practices in architecture (MVI), modular Android app development, and Compose-based UI design.

---

## ✨ Features

- 📄 Add and edit notes
- ⏰ Set reminders for any note
- 🔔 Local notifications at scheduled times
- 📱 Built 100% with **Jetpack Compose**
- 💉 Dependency injection with **Hilt**
- 🗄 Local database persistence using **Room**
- ✅ Unit testing and a well-defined architecture

---

## 🧱 Architecture

This app follows the **Model-View-ViewModel (MVVM)** architecture for clear separation of responsibilities:

### • **Model**
- Data classes representing Note items and Reminder logic
- Room database and DAO to persist notes locally

### • **ViewModel**
- Exposes UI state via `StateFlow`
- Handles user events (adding/editing notes, setting reminders)
- Interacts with the Repository layer

### • **View (Compose UI)**
- Observes `ViewModel` state
- Uses recomposable components for clean UI updates

---

## 🛠 Tech Stack

| Technology       | Usage                          |
|------------------|--------------------------------|
| Jetpack Compose  | Declarative UI Framework       |
| Hilt             | Dependency Injection           |
| Room             | Local Database (SQLite)        |
| WorkManager      | Scheduled background tasks     |
| ViewModel        | State holder for UI            |
| Kotlin Coroutines| Asynchronous programming       |

---

## 🔔 Reminder & Notification Flow

1. User creates or edits a note and sets a future reminder time.
2. Reminder time is stored in the local Room database.
3. App schedules a local notification using **WorkManager** or **AlarmManager**.
4. At the scheduled time, a **notification** is triggered even if the app is backgrounded or killed.

---

## 📸 Screenshots

_Include these images to showcase the app flow visually_

| Notes Screen | Add/Edit Notes Screen | Set Reminder |
|----------|---------------|--------------|
| ![Add Note](screenshots/add_note.png) | ![Set Reminder](screenshots/set_reminder.png) | ![Notification](screenshots/notification.png) |

📁 _Place your screenshots in `/screenshots` or embed remote image URLs._

---

## 🎥 Demo Video

_Include a short video or screen recording showcasing the app — optional but highly recommended._

▶️ [Watch Demo](https://drive.google.com/file/d/1MxNIRXplo0DvahLtHIFSnJW5XX-Grwjw/view?usp=drive_link)

---

## 📦 APK

You can download the latest build here:

👉 [Download APK](https://github.com/your-username/note-reminder-app/releases)

_Or manually build it using:_

```bash
./gradlew assembleDebug

