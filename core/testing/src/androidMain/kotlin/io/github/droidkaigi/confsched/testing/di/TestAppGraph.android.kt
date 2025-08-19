package io.github.droidkaigi.confsched.testing.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.createGraph
import io.github.droidkaigi.confsched.data.DataScope
import io.github.droidkaigi.confsched.data.about.FakeBuildConfigProvider
import io.github.droidkaigi.confsched.data.about.FakeLicensesJsonReader
import io.github.droidkaigi.confsched.data.contributors.DefaultContributorsApiClient
import io.github.droidkaigi.confsched.data.sessions.DefaultSessionsApiClient
import kotlinx.coroutines.CoroutineDispatcher

@DependencyGraph(
    scope = AppScope::class,
    additionalScopes = [DataScope::class],
    isExtendable = true,
    excludes = [
        DefaultSessionsApiClient::class,
        DefaultContributorsApiClient::class,
        CoroutineDispatcher::class,
    ],
)
internal interface AndroidTestAppGraph : TestAppGraph {
    @Provides
    fun provideContext(): Context {
        val testContext = ApplicationProvider.getApplicationContext<Context>()
        // Workaround for "Android context is not initialized"
        // FYI: https://youtrack.jetbrains.com/issue/CMP-6676/Android-context-is-not-initialized-when-removing-AndroidContextProvider
        val providerClass = Class.forName("org.jetbrains.compose.resources.AndroidContextProvider")
        val provider = providerClass.getDeclaredConstructor().newInstance()
        providerClass.getMethod("access\$setANDROID_CONTEXT\$cp", Context::class.java)
            .invoke(provider, testContext)
        return testContext
    }

    @Provides
    fun provideFakeBuildConfigProvider(): FakeBuildConfigProvider = FakeBuildConfigProvider()

    @Provides
    fun provideFakeLicensesJsonReader(): FakeLicensesJsonReader = FakeLicensesJsonReader()
}

internal actual fun createTestAppGraph(): TestAppGraph {
    return createGraph<AndroidTestAppGraph>()
}
