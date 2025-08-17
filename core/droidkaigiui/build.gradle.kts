plugins {
    id("droidkaigi.primitive.kmp")
    id("droidkaigi.primitive.kmp.ios")
    id("droidkaigi.primitive.kmp.compose")
    id("droidkaigi.primitive.kmp.compose.resources")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.designsystem)
            implementation(projects.core.model)
            implementation(libs.soilQueryCompose)
            implementation(libs.soilReacty)

            api(libs.coil)
            api(libs.coilNetwork)
        }
    }
}
