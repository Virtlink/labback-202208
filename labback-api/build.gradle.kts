plugins {
    kotlin("jvm")
    `java-library`
    jacoco
    `project-report`
    `maven-publish`
    idea
}

//////////////////
// Dependencies //
//////////////////
dependencies {
    // NOTE: Versions are specified in gradle/libs.versions.toml

    // Logging
    implementation      (libs.slf4j.api)
    implementation      (libs.kotlin.logging)
    implementation      (libs.logback.core)     // This should not enable logging, just provide the API

    // HTTP
    implementation      (enforcedPlatform(libs.http4k.bom))
    implementation      (libs.http4k.core)
    implementation      (libs.http4k.contract)
    implementation      (libs.http4k.server.undertow)
    implementation      (libs.http4k.format.jackson)

    // Testing
    testImplementation  (libs.junit.core)
    testImplementation  (libs.junit.api)
    testImplementation  (libs.junit.engine)
    testImplementation  (libs.junit.params)
    testImplementation  (kotlin("test"))

    testImplementation  (libs.awaitility.core)
    testImplementation  (libs.awaitility.kotlin)
}

/////////////////////////
// Export test classes //
/////////////////////////
tasks.register<Jar>("testJar") {
    archiveClassifier.set("tests")
    from(sourceSets.test.get().output)
}
configurations.create("tests")  // NOTE: Transitive dependencies are not included
artifacts {
    add("tests", tasks.named("testJar"))
}
