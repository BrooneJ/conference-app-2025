package io.github.droidkaigi.confsched

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import soil.query.SwrCachePlus
import soil.query.SwrCacheScope
import soil.query.annotation.ExperimentalSoilQueryApi
import soil.query.compose.SwrClientProvider

@Composable
expect fun rememberExternalNavController(): ExternalNavController

context(appGraph: AppGraph)
@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalSoilQueryApi::class)
@Composable
fun KaigiApp() {
    SwrClientProvider(SwrCachePlus(SwrCacheScope())) {
        KaigiTheme {
            Surface {
                KaigiAppUi()
            }
        }
    }
}
