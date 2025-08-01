pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://storage.googleapis.com/download.flutter.io")}
        }
}

rootProject.name = "new_feature_android"
include(":app")

// Flutter Module 설정
val flutterProjectRoot = rootDir.parentFile
val flutterModuleName = "new_feature_flutter_module"
val flutterModulePath = File(flutterProjectRoot, flutterModuleName)

if (flutterModulePath.exists()) {
    val flutterAndroidPath = File(flutterModulePath, ".android")
    val includeFlutterScript = File(flutterAndroidPath, "include_flutter.groovy")

    if (includeFlutterScript.exists()) {
        println("✅ Flutter module exists: $flutterModuleName")
        apply(from = includeFlutterScript)
    } else {
        println("⚠️ Flutter module found but, not initialized.")
        println("please run flutter pub get.")
    }
} else {
    println("⚠️ Flutter module not found: $flutterModuleName")
}
