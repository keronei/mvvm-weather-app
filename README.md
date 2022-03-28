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