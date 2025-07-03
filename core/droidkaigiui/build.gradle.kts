plugins {
    id("droidkaigi.primitive.kmp")
    id("droidkaigi.primitive.kmp.compose")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(libs.soilQueryCompose)
            implementation(libs.soilReacty)
            implementation(libs.navigation3Ui)
            implementation(libs.navigation3Runtime)
        }

        androidMain.dependencies {
            implementation(libs.lifecycleViewmodelNavigation3)
        }
    }
}
