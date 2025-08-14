@file:Suppress("UNUSED")

package io.github.droidkaigi.confsched.droidkaigiui.architecture

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.safeDrawingWithBottomNavBar

sealed interface SoilFallback {
    val suspenseFallback: @Composable context(SoilSuspenseContext) BoxScope.() -> Unit
    val errorFallback: @Composable context(SoilErrorContext) BoxScope.() -> Unit
}

object SoilFallbackDefaults {
    fun empty(): SoilFallback = Empty

    @Composable
    fun appBar(
        title: String,
        onBackClick: (() -> Unit)? = null,
        appBarSize: AppBarSize = AppBarSize.Default,
        // Allowing WindowInsets to be overridden to prevent layout jump/glitches
        // when navigating between screens with and without a bottom navigation bar.
        windowInsets: WindowInsets = WindowInsets.safeDrawingWithBottomNavBar,
    ): SoilFallback = AppBar(
        title = title,
        onBackClick = onBackClick,
        size = appBarSize,
        windowInsets = windowInsets,
    )

    fun simple(): SoilFallback = Simple

    fun custom(
        suspenseFallback: @Composable context(SoilSuspenseContext) BoxScope.() -> Unit,
        errorFallback: @Composable context(SoilErrorContext) BoxScope.() -> Unit,
    ): SoilFallback = Custom(
        suspenseFallback = suspenseFallback,
        errorFallback = errorFallback,
    )
}

private object Empty : SoilFallback {
    override val suspenseFallback: @Composable context(SoilSuspenseContext) BoxScope.() -> Unit = {}
    override val errorFallback: @Composable context(SoilErrorContext) BoxScope.() -> Unit = {}
}

private object Simple : SoilFallback {
    override val suspenseFallback: @Composable context(SoilSuspenseContext) BoxScope.() -> Unit
        get() = { DefaultSuspenseFallbackContent() }
    override val errorFallback: @Composable context(SoilErrorContext) BoxScope.() -> Unit
        get() = { DefaultErrorFallbackContent() }
}

private class AppBar(
    val title: String,
    val onBackClick: (() -> Unit)?,
    val size: AppBarSize,
    val windowInsets: WindowInsets,
) : SoilFallback {
    override val suspenseFallback: @Composable context(SoilSuspenseContext) BoxScope.() -> Unit = {
        AppBarFallbackScaffold(
            title = title,
            onBackClick = onBackClick,
            appBarSize = size,
            windowInsets = windowInsets,
        ) { innerPadding ->
            DefaultSuspenseFallbackContent(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

    override val errorFallback: @Composable context(SoilErrorContext) BoxScope.() -> Unit = {
        AppBarFallbackScaffold(
            title = title,
            onBackClick = onBackClick,
            appBarSize = size,
            windowInsets = windowInsets,
        ) { innerPadding ->
            DefaultErrorFallbackContent(
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

private class Custom(
    override val suspenseFallback: @Composable context(SoilSuspenseContext) BoxScope.() -> Unit,
    override val errorFallback: @Composable context(SoilErrorContext) BoxScope.() -> Unit,
) : SoilFallback
