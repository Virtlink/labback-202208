plugins {
    id("com.gradle.enterprise") version("3.10.1")
}

rootProject.name = "labback-project"

include(":labback-api")         // The Labback API interfaces and HTTP implementations
