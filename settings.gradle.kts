import java.util.Properties

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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://repo.repsy.io/mvn/tyzar/mantra-either")
            val credProp = Properties()
            credProp.load(File("credential.properties").inputStream())

            credentials {
                username = credProp.getProperty("repsy_username")
                password = credProp.getProperty("repsy_pass")
            }
        }
    }
}

rootProject.name = "mantra_either"
include(":app", "mantra-either")
