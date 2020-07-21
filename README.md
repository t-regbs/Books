# Books
A simple book finder android application to search for books using api from [Google Books Api](https://developers.google.com/books)

## Why this project?

This project is built to show how to build modern android applications following [Guide to app architecture](https://developer.android.com/jetpack/docs/guide) with kotlin and some of the android [jetpack libraries](https://developer.android.com/jetpack).

## Getting started

After cloning the project, in your projects root directory, create a file `apikey.properties`.
You'll need an api key from [Google Books Api](https://developers.google.com/books).
Add the line `Books_ApiKey = "YOUR-API-KEY"` to that file.

Libraries Used
--------------
* [Foundation][0] - Components for core system capabilities, Kotlin extensions and support for
  multidex and automated testing.
  * [AppCompat][1] - Degrade gracefully on older versions of Android.
  * [Android KTX][2] - Write more concise, idiomatic Kotlin code.
* [Architecture][10] - A collection of libraries that help you design robust, testable, and
  maintainable apps. Start with classes for managing your UI component lifecycle and handling data
  persistence.
  * [Data Binding][11] - Declaratively bind observable data to UI elements.
  * [LiveData][13] - Build data objects that notify views when the underlying database changes.
  * [Navigation][14] - Handle everything needed for in-app navigation.
  * [Room][16] - Access your app's SQLite database with in-app objects and compile-time checks.
  * [Paging][3] - Load and display small chunks of data at a time.
  * [ViewModel][17] - Store UI-related data that isn't destroyed on app rotations. Easily schedule
     asynchronous tasks for optimal execution.
* [UI][30] - Details on why and how to use UI Components in your apps - together or separate
  * [Fragment][34] - A basic unit of composable UI.
  * [Recyclerview][35] - Display large sets of data in your UI while minimizing memory usage.
* Third party
  * [Koin][18] for dependency injection.
  * [Glide][90] for image loading.
  * [Moshi][4]  A modern JSON library for Android and Java. It makes it easy to parse JSON into Java objects.
  * [Retrofit][5] A type-safe HTTP client for Android.
  * [Timber][6] A logger with a small, extensible API which provides utility on top of Android's normal Log class.
  * [Kotlin Coroutines][91] for managing background threads with simplified code and reducing needs for callbacks.

[11]: https://developer.android.com/topic/libraries/data-binding/
[34]: https://developer.android.com/guide/components/fragments
[35]: https://developer.android.com/jetpack/androidx/releases/recyclerview
[13]: https://developer.android.com/topic/libraries/architecture/livedata
[14]: https://developer.android.com/topic/libraries/architecture/navigation/
[16]: https://developer.android.com/topic/libraries/architecture/room
[17]: https://developer.android.com/topic/libraries/architecture/viewmodel
[18]: https://insert-koin.io/
[0]: https://developer.android.com/jetpack/components
[1]: https://developer.android.com/topic/libraries/support-library/packages#v7-appcompat
[2]: https://developer.android.com/kotlin/ktx
[3]: https://developer.android.com/topic/libraries/architecture/paging
[4]: https://github.com/square/moshi
[5]: https://square.github.io/retrofit/
[6]: https://github.com/JakeWharton/timber
[90]: https://bumptech.github.io/glide/
[91]: https://kotlinlang.org/docs/reference/coroutines-overview.html
[30]: https://developer.android.com/guide/topics/ui
[10]: https://developer.android.com/jetpack/arch/

Support
-------

If you like the project, do well to star it and also keep a watch.
