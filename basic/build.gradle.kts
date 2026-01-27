plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
}

group = "io.intellij.kotlin.basic.dsa"

val projectJdkVersion = libs.versions.java.get().toInt()
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(projectJdkVersion)
    }
}

kotlin {
    jvmToolchain(projectJdkVersion)
}

dependencies {
    api(libs.guava)
    api(libs.slf4j.api)
    api(libs.logback.classic)
    api(libs.logback.core)

    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform() {
        includeEngines("junit-jupiter")
    }

    testLogging {
        events("passed", "skipped", "failed")
        showExceptions = true
        showCauses = true
        showStackTraces = true

        // 设置日志级别
        showStandardStreams = true
    }

    systemProperty("logback.configurationFile", "logback-test.xml")

}
