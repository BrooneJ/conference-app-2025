plugins {
    alias(libs.plugins.kotlinJvm)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=com.squareup.kotlinpoet.ExperimentalKotlinPoetApi")
    }
}

dependencies {
    implementation(libs.kspSymbolProcessingApi)
    implementation(libs.kotlinPoetKsp)
}
