package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme

@OptIn(ExperimentalCoilApi::class)
@Composable
fun KaigiPreviewContainer(
    content: @Composable () -> Unit,
) {
    KaigiTheme {
        Surface {
            val previewHandler = AsyncImagePreviewHandler {
                ColorImage(Color.Red.toArgb())
            }

            CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
                content()
            }
        }
    }
}
