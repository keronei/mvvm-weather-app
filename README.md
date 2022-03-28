# Weather App

This is a simple app that implements the following features

- [x] List Cities - Comes with ~42K cities even without data connection.
- [x] Connect to Internet and fetch data for up to 7 days ahead, with 48 hours of hourly weather updates
- [x] Temperatures on the listed cities when you have connected to internet in the past 48 hours.
- [x] Details of city weather with listing upto 7 days ahead
- [x] Favouriting city from detail view, gets listed on the top
- [x] Notification of hourly weather update for every favourited city.
- [x] Ready for extension with cities inbuilt cities - only top 20 are displayed.

### Running
You will need a `key.properties` file with `API_KEY=abckey`, you can get the key from [openweathermap](https://openweathermap.org/)
Configuration for playstore key if need be

### Screenshots

<img src="https://user-images.githubusercontent.com/17080971/160416550-997e4bd1-836d-4db6-8f8e-e09788458ab6.png" width="250" height="470"/> <img src="https://user-images.githubusercontent.com/17080971/160416564-97eed052-6869-4fff-9e31-c1539a66a63a.png" width="250" height="470"/> <img src="https://user-images.githubusercontent.com/17080971/160416578-3afcb8a9-5844-452e-be14-a26fb97544d5.png" width="250" height="470"/>
<img src="https://user-images.githubusercontent.com/17080971/160416589-342b5dea-22f7-41b6-843b-5e9b38d4d657.png" width="250" height="470"/>


## Tech-stack

* Gradle
    * [Gradle Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html) - For reference purposes, here's an [article explaining the migration](https://medium.com/@evanschepsiror/migrating-to-kotlin-dsl-4ee0d6d5c977).
    * Plugins
        * [Ktlint](https://github.com/JLLeitschuh/ktlint-gradle) - creates convenient tasks in your Gradle project that run ktlint checks or do code auto format.
        * [Detekt](https://github.com/detekt/detekt) - a static code analysis tool for the Kotlin programming language.
        * [Spotless](https://github.com/diffplug/spotless) - format java, groovy, markdown and license headers using gradle.
        * [Dokka](https://github.com/Kotlin/dokka) - a documentation engine for Kotlin, performing the same function as javadoc for Java.
        * [jacoco](https://github.com/jacoco/jacoco) - a Code Coverage Library.
        * [Gradle Versions](https://github.com/ben-manes/gradle-versions-plugin) - provides a task to determine which dependencies have updates. Additionally, the plugin checks for updates to Gradle itself.
        
### Thanks to [SimpleMaps](https://simplemaps.com/) for the comprehensive city data.
