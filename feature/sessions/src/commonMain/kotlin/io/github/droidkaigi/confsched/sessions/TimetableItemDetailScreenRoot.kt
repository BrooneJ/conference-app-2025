package io.github.droidkaigi.confsched.sessions

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import io.github.droidkaigi.confsched.common.compose.rememberEventFlow
import io.github.droidkaigi.confsched.droidkaigiui.SoilDataBoundary
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import kotlinx.serialization.Serializable
import soil.query.compose.rememberQuery

@Serializable
data class TimetableItemDetailNavKey(val id: TimetableItemId) : NavKey

context(screenContext: TimetableItemDetailScreenContext)
@Composable
fun TimetableItemDetailScreenRoot(
    timetableItemId: TimetableItemId
) {
    SoilDataBoundary(
        state = rememberQuery(
            key = screenContext.timetableItemQueryKeyFactory.create(timetableItemId),
        ),
        errorFallback = {}
    ) { timetableItem  ->
        val eventFlow = rememberEventFlow<TimetableItemDetailScreenEvent>()

        val uiState = timetableItemDetailScreenPresenter(
            eventFlow = eventFlow,
            timetableDetail = timetableItem
        )

        TimetableItemDetailScreen(
            uiState = uiState,
        )
    }
}
