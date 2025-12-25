pluginManagement {
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        gradlePluginPortal()
    }
}

// plugins {
//     id 'org.gradle.toolchains.foojay-resolver-convention' version "${foojay_version}"
// }

plugins {
    id("com.gradle.develocity") version ("4.3")
}

rootProject.name = "kotlin-dsa"

include("basic")
include("project")