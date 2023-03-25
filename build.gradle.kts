import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import java.util.Properties
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import com.adarshr.gradle.testlogger.theme.ThemeType

buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    kotlin("multiplatform") version "1.7.10" apply false
    kotlin("plugin.serialization") version "1.7.10" apply false
    java
    jacoco
    `maven-publish`
    alias(libs.plugins.gitversion)
    alias(libs.plugins.testlogger)
    alias(libs.plugins.versions)
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.palantir.git-version")
    apply(plugin = "com.github.ben-manes.versions")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "jacoco")
    apply(plugin = "project-report")
    apply(plugin = "com.adarshr.test-logger")

    val gitVersion: groovy.lang.Closure<String> by extra

    group = "nl.tudelft"
    version = gitVersion()

    repositories {
        google()
        mavenCentral()
    }

    /////////////////////////
    // Dependency Versions //
    /////////////////////////
    /**
     * Our best guess for what constitutes an unstable dependency version.
     */
    fun isUnstableVersion(version: String): Boolean {
        // An unstable version has a named part, either starting with a dash or a dot. For example `1.0-alpha3` or `1.0.DEV`.
        val regex = Regex("^[0-9]+(\\.[0-9]+)*(\\+[0-9a-zA-Z\\.]+)?(\\-.*)|(\\.[a-zA-Z].*)$")
        return regex.matchEntire(version) != null
    }

    tasks.withType<DependencyUpdatesTask> {
        rejectVersionIf {
            // Reject if the current version is a stable version but the candidate is not
            !isUnstableVersion(currentVersion) && isUnstableVersion(candidate.version)
        }
        gradleReleaseChannel = "current"
    }

    ////////////////
    // Publishing //
    ////////////////
    publishing {
        publications {
            create<MavenPublication>("library") {
                from(components["java"])
            }
        }
        repositories {
            maven {
                url = uri("${System.getenv("CI_API_V4_URL")}/projects/6800/packages/maven")
                name = "GitLab"
                credentials(HttpHeaderCredentials::class) {
                    name = "Job-Token"
                    value = System.getenv("CI_JOB_TOKEN")
                }
                authentication {
                    create<HttpHeaderAuthentication>("header")
                }
            }
        }
    }

    //////////
    // Java //
    //////////
    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(11))
        withSourcesJar()
        withJavadocJar()
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
    }

    /////////////
    // Testing //
    /////////////
    tasks.test {
        useJUnitPlatform()
        finalizedBy(tasks.jacocoTestReport)
        testlogger {
            theme = ThemeType.MOCHA
        }
    }

    ///////////////////////////////
    // Build Properties resource //
    ///////////////////////////////
    tasks {
        val createProperties by registering {
            dependsOn("processResources")
            doLast {
                val revision = "git describe --always".runCommand().trim()
                val fullRevision = "git log -n1 --format=%H".runCommand().trim()
                val propertiesDir = "$buildDir/resources/main"
                mkdir(propertiesDir)
                file("$propertiesDir/version.properties").writer().use { w ->
                    val p = Properties()
                    p["version"] = project.version.toString()
                    p["revision"] = revision
                    p["full-revision"] = fullRevision
                    p["build-time"] =
                        ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS).format(
                            DateTimeFormatter.ISO_OFFSET_DATE_TIME
                        )
                    p.store(w, "Version information")
                }
            }
        }

        classes {
            dependsOn(createProperties)
        }
    }
}

////////////////
// Build Scan //
////////////////
gradleEnterprise {
    buildScan {
        // Git Commit ID
        "git rev-parse --verify HEAD".runCommand().takeIf { it.isNotEmpty() }?.let { commitId ->
            value("Git Commit ID", commitId)
        }

        // Git Branch Name
        "git rev-parse --abbrev-ref HEAD".runCommand().takeIf { it.isNotEmpty() }?.let { branchName ->
            value("Git Branch Name", branchName)
        }

        // Git Status
        "git status --porcelain".runCommand().takeIf { it.isNotEmpty() }?.let { status ->
            tag("dirty")
            value("Git Status", status)
        }
    }
}

///////////////
// Utilities //
///////////////
fun String.runCommand(workingDir: File = file("./")): String {
    val parts = this.split("\\s".toRegex())
    val proc = ProcessBuilder(*parts.toTypedArray())
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()

    proc.waitFor(1, TimeUnit.MINUTES)
    return proc.inputStream.bufferedReader().readText()
}
