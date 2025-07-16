package io.github.droidkaigi.confsched.testing.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.createGraph
import io.github.droidkaigi.confsched.data.DataScope
import io.github.droidkaigi.confsched.data.contributors.DefaultContributorsApiClient
import io.github.droidkaigi.confsched.data.sessions.DefaultSessionsApiClient

@DependencyGraph(
    scope = AppScope::class,
    additionalScopes = [DataScope::class],
    isExtendable = true,
    excludes = [
        DefaultSessionsApiClient::class,
        DefaultContributorsApiClient::class,
    ],
)
interface AndroidTestAppGraph : TestAppGraph {
    @Provides
    fun provideContext(): Context {
        return ApplicationProvider.getApplicationContext()
    }
}

actual fun createTestAppGraph(): TestAppGraph {
    return createGraph<AndroidTestAppGraph>()
}
