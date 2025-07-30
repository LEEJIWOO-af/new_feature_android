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

// setup Flutter Module
val flutterProjectRoot = rootDir.parentFile
val flutter = File(flutterProjectRoot, "new_feature_flutter_module")
val flutterAndroid = File(flutter, ".android")

if (flutterAndroid.exists()) {
    // Flutter module import
    val flutterSettings = File(flutterAndroid, "include_flutter.groovy")
    if (flutterSettings.exists()) {
        apply(from = flutterSettings)
    }
}