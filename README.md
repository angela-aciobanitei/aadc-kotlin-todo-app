# aadc-kotlin-todo-app

A toy app created when preparing for the AADC Exam

## App Features
*   [MVVM Architecture Pattern](https://medium.com/upday-devs/android-architecture-patterns-part-3-model-view-viewmodel-e7eeee76b73b), [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) and [ViewModels](https://developer.android.com/topic/libraries/architecture/viewmodel)
*   [Repository Pattern](https://www.raywenderlich.com/3595916-clean-architecture-tutorial-for-android-getting-started) to mediate data operations
*   [Room](https://developer.android.com/topic/libraries/architecture/room) for data persistence
*   [Navigation Component](https://developer.android.com/guide/navigation) with [SafeArgs](https://developer.android.com/guide/navigation/navigation-pass-data) to handle app navigation
*   [Paging Library](https://developer.android.com/topic/libraries/architecture/paging) to load partial data on demand
*   [AlarmManager](https://developer.android.com/training/scheduling/alarms) and [BroadcastReceivers](https://developer.android.com/guide/components/broadcasts) to schedule repeating alarms
*   [Notifications](https://developer.android.com/guide/topics/ui/notifiers/notifications), [Snackbars](https://developer.android.com/reference/android/support/design/widget/Snackbar), [Menus](https://developer.android.com/guide/topics/ui/menus), [RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview)
*   [Settings screen](https://developer.android.com/guide/topics/ui/settings) and [SharedPreferences](https://developer.android.com/training/data-storage/shared-preferences)


## Exam Content
### Android Core
- [x] Understand the architecture of the Android system
- [x] Describe the basic [building blocks](https://developer.android.com/guide/components/fundamentals) of an Android app
- [x] Use [Android Jetpack](https://developer.android.com/jetpack/docs/getting-started) and [AndroidX](https://developer.android.com/jetpack/androidx) libraries
- [x] Display simple messages in a popup using a [Toast](https://developer.android.com/guide/topics/ui/notifiers/toasts) or a [Snackbar](https://developer.android.com/reference/android/support/design/widget/Snackbar)
- [x] Display a message outside your app's UI using [Notifications](https://developer.android.com/training/notify-user/build-notification)
- [x] Schedule a background task using [JobScheduler](https://codelabs.developers.google.com/codelabs/android-training-job-scheduler) or [WorkManager](https://codelabs.developers.google.com/codelabs/android-workmanager)
*   Understand how to [localize](https://developer.android.com/guide/topics/resources/localization) an app

### User  Interface
- [x] Understand the Android activity [lifecycle](https://codelabs.developers.google.com/codelabs/kotlin-android-training-lifecycles-logging)
- [x] Create an Activity that displays a Layout
- [x] Construct a UI with [ConstraintLayout](https://developer.android.com/training/constraint-layout/)
- [x] Display items in a [RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview)
- [x] Bind local data to a RecyclerView list using the [Paging](https://codelabs.developers.google.com/codelabs/android-paging) library
- [x] Implement [menu-based navigation](https://developer.android.com/guide/navigation/navigation-ui#Tie-navdrawer)
- [x] Implement [drawer navigation](https://developer.android.com/guide/navigation/navigation-ui#add_a_navigation_drawer)
- [x] Implement a custom app [theme](https://developer.android.com/guide/topics/ui/look-and-feel/themes)
* Create a [custom View](https://developer.android.com/guide/topics/ui/custom-components) class and add it to a Layout
* Add [accessibility hooks](https://developer.android.com/guide/topics/ui/accessibility/custom-views) to a custom View
* Apply content descriptions to views for [accessibility](https://codelabs.developers.google.com/codelabs/basic-android-accessibility)

### Data Management
- [x] Define data using [Room entities](https://developer.android.com/training/data-storage/room/defining-data)
- [x] Access Room database with data access object [(DAO)](https://developer.android.com/training/data-storage/room/accessing-data)
- [x] Observe and respond to changing data using [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
- [x] Use a [Repository](https://codelabs.developers.google.com/codelabs/kotlin-android-training-repository) to mediate data operations
- [x] Create persistent [Preference](https://developer.android.com/training/data-storage/shared-preferences) data from user input
- [x] Understand how to change the behavior of the app based on [user preferences](https://developer.android.com/guide/topics/ui/settings)
* Read and parse raw resources or asset files

### Debugging

- [x] Understand the basic [debugging](https://developer.android.com/studio/debug/) techniques available in Android Studio
- [x] Debug and fix issues with an app's functional behavior and usability
- [x] Use the [System Log](https://developer.android.com/studio/debug/am-logcat) to output debug information
- [x] Use breakpoints and inspect variables using [Android Studio](https://codelabs.developers.google.com/codelabs/android-training-using-debugger)

### Testing
- [x] Thoroughly understand the [fundamentals of testing](https://developer.android.com/training/testing/fundamentals)
- [x] Be able to write useful [local](https://codelabs.developers.google.com/codelabs/advanced-android-kotlin-training-testing-basics) [JUnit tests](https://codelabs.developers.google.com/codelabs/advanced-android-kotlin-training-testing-test-doubles)
- [x] Understand the [Espresso](https://developer.android.com/training/testing/espresso) UI test framework
- [x] Write useful [automated](https://codelabs.developers.google.com/codelabs/advanced-android-kotlin-training-testing-survey) Android tests



