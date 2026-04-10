pluginManagement {
  repositories {
    maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
    gradlePluginPortal()
  }
}

plugins {
  id("com.gradle.develocity") version ("4.3")
}

rootProject.name = "kotlin-dsa"

include("basic")
include("project")