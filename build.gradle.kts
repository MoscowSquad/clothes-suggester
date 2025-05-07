plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "1.8.20"
    jacoco
}

group = "com.usc.team"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.insert-koin:koin-core:4.0.2")
    implementation("io.arrow-kt:arrow-core:2.0.1")
    implementation("io.arrow-kt:arrow-fx-coroutines:2.0.1")
    implementation("io.insert-koin:koin-core:4.0.2")
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.9.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.12.0-M1")
    testImplementation("io.mockk:mockk:1.13.16")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
    testImplementation("com.google.truth:truth:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    implementation("io.ktor:ktor-client-core:2.3.13")
    implementation("io.ktor:ktor-client-cio:2.3.13")
    testImplementation("io.ktor:ktor-client-mock:2.3.13")

    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    val ktor_version = "2.3.10"
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation(kotlin("stdlib-jdk8"))


}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
    }
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        csv.required.set(true)
        html.required.set(true) // Added HTML report
    }
    doLast {
        println("Coverage report: file://${layout.buildDirectory}/reports/jacoco/test/html/index.html") // Adds clickable report link after test
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.test)
    violationRules {
        classDirectories.setFrom(
            classDirectories.files.map {
                fileTree(it) {
                    exclude("**/models/**")
                    exclude("**/di/**")
                }
            }
        )

        rule {
            limit {
                minimum = "0.8".toBigDecimal()
            }
        }
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.8".toBigDecimal()
            }
        }
        rule {
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.8".toBigDecimal()
            }
        }
        rule {
            limit {
                counter = "METHOD"
                value = "COVEREDRATIO"
                minimum = "0.8".toBigDecimal()
            }
        }
    }
}

jacoco {
    toolVersion = "0.8.13"
}

kotlin {
    jvmToolchain(17)
}