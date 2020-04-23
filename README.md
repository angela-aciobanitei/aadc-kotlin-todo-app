# AADC Exam Toy App

A toy app created when preparing for the AADC Exam

## Core Features
*   Implement the [MVVM Architecture Pattern](https://medium.com/upday-devs/android-architecture-patterns-part-3-model-view-viewmodel-e7eeee76b73b)
*   Mediate data operations with the [Repository Pattern](https://www.raywenderlich.com/3595916-clean-architecture-tutorial-for-android-getting-started) 
*   Observe and respond to changing data using [LiveData](https://developer.android.com/topic/libraries/architecture/livedata).
*   Persist data with [Room](https://developer.android.com/topic/libraries/architecture/room)
*   Improve app performance with [Kotlin coroutines](https://developer.android.com/kotlin/coroutines) to manage [long-running tasks](https://codelabs.developers.google.com/codelabs/kotlin-coroutines)
*   Handle app navigation with [Navigation Component](https://developer.android.com/guide/navigation) and [SafeArgs](https://developer.android.com/guide/navigation/navigation-pass-data) 
*   Display [Menus](https://developer.android.com/guide/topics/ui/menus) and handle [menu-based navigation](https://developer.android.com/guide/navigation/navigation-ui#Tie-navdrawer)
*   Add a navigation drawer and implement [drawer navigation](https://developer.android.com/guide/navigation/navigation-ui#add_a_navigation_drawer) 
*   Display simple messages in a popup using a [Toast](https://developer.android.com/guide/topics/ui/notifiers/toasts) or a [Snackbar](https://developer.android.com/reference/android/support/design/widget/Snackbar)
*   Display a message outside app's UI using Notifications [Notifications](https://developer.android.com/guide/topics/ui/notifiers/notifications)
*   Schedule repeating alarms with [AlarmManager](https://developer.android.com/training/scheduling/alarms) and [BroadcastReceivers](https://developer.android.com/guide/components/broadcasts)
*   Display items in a [RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview)
*   Bind local data to a RecyclerView list using the [Paging Library](https://codelabs.developers.google.com/codelabs/android-paging) 
*   Handle user preferences with a [Settings screen](https://developer.android.com/guide/topics/ui/settings) and [SharedPreferences](https://developer.android.com/training/data-storage/shared-preferences)


## [Exam Study Guide Kotlin](https://developers.google.com/certification/associate-android-developer/study-guide)
### Android Core
- [x] Understand the architecture of the Android system
- [x] Describe the basic [building blocks](https://developer.android.com/guide/components/fundamentals) of an Android app
- [x] Use [Android Jetpack](https://developer.android.com/jetpack/docs/getting-started) and [AndroidX](https://developer.android.com/jetpack/androidx) libraries
- [x] Display simple messages in a popup using a [Toast](https://developer.android.com/guide/topics/ui/notifiers/toasts) or a [Snackbar](https://developer.android.com/reference/android/support/design/widget/Snackbar)
- [x] Display a [message](https://developer.android.com/training/notify-user/build-notification) outside your app's UI using [Notifications](https://codelabs.developers.google.com/codelabs/advanced-android-kotlin-training-notifications)
- [x] Schedule a background task using [JobScheduler](https://codelabs.developers.google.com/codelabs/android-training-job-scheduler) or [WorkManager](https://codelabs.developers.google.com/codelabs/android-workmanager)
- [ ] Understand how to [localize](https://developer.android.com/guide/topics/resources/localization) an app

### User  Interface
- [x] Understand the Android activity [lifecycle](https://codelabs.developers.google.com/codelabs/kotlin-android-training-lifecycles-logging)
- [x] Create an Activity that displays a Layout
- [x] Construct a UI with [ConstraintLayout](https://developer.android.com/training/constraint-layout/)
- [x] Display items in a [RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview)
- [x] Bind local data to a [RecyclerView](https://codelabs.developers.google.com/codelabs/kotlin-android-training-recyclerview-fundamentals) list using the [Paging](https://codelabs.developers.google.com/codelabs/android-paging) [library](https://developer.android.com/topic/libraries/architecture/paging)
- [x] Implement [menu-based navigation](https://developer.android.com/guide/navigation/navigation-ui#Tie-navdrawer)
- [x] Implement [drawer navigation](https://developer.android.com/guide/navigation/navigation-ui#add_a_navigation_drawer)
- [x] Implement a [custom](https://codelabs.developers.google.com/codelabs/kotlin-android-training-styles-and-themes) app [theme](https://developer.android.com/guide/topics/ui/look-and-feel/themes)
- [ ] Create a [custom View](https://developer.android.com/guide/topics/ui/custom-components) class and add it to a Layout
- [ ] Add [accessibility hooks](https://developer.android.com/guide/topics/ui/accessibility/custom-views) to a custom View
- [ ] Apply content descriptions to views for [accessibility](https://codelabs.developers.google.com/codelabs/basic-android-accessibility)

### Data Management
- [x] Define data using [Room entities](https://developer.android.com/training/data-storage/room/defining-data)
- [x] Access Room database with data access object [(DAO)](https://developer.android.com/training/data-storage/room/accessing-data)
- [x] Observe and respond to changing data using [LiveData](https://codelabs.developers.google.com/codelabs/kotlin-android-training-live-data)
- [x] Use a [Repository](https://codelabs.developers.google.com/codelabs/kotlin-android-training-repository) to mediate data operations
- [x] Create persistent [Preference](https://developer.android.com/training/data-storage/shared-preferences) data from user input
- [x] Understand how to change the behavior of the app based on [user preferences](https://developer.android.com/guide/topics/ui/settings)
- [ ] Read and parse raw resources or asset files

### Debugging

- [x] Understand the basic [debugging](https://developer.android.com/studio/debug/) techniques available in Android Studio
- [x] Debug and fix issues with an app's functional behavior and usability
- [x] Use the [System Log](https://developer.android.com/studio/debug/am-logcat) to output debug information
- [x] Use breakpoints and inspect variables using [Android Studio](https://codelabs.developers.google.com/codelabs/android-training-using-debugger)

### Testing
- [x] Thoroughly understand the [fundamentals of testing](https://developer.android.com/training/testing/fundamentals)
- [x] Write useful [local JUnit tests](https://developer.android.com/training/testing/unit-testing/local-unit-tests) 
- [x] Build [instrumented unit tests](https://developer.android.com/training/testing/unit-testing/instrumented-unit-tests)
- [x] Use [Test doubles](https://codelabs.developers.google.com/codelabs/advanced-android-kotlin-training-testing-test-doubles)
- [x] Understand the [Espresso](https://developer.android.com/training/testing/espresso) UI test framework
- [x] Write useful [automated](https://codelabs.developers.google.com/codelabs/advanced-android-kotlin-training-testing-survey) Android tests



