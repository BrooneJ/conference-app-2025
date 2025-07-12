package io.github.droidkaigi.confsched.favorites

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.common.compose.rememberEventFlow
import io.github.droidkaigi.confsched.droidkaigiui.SoilDataBoundary
import soil.query.compose.rememberSubscription

context(screenContext: FavoritesScreenContext)
@Composable
fun FavoritesScreenRoot() {
    SoilDataBoundary(
        state1 = rememberSubscription(screenContext.timetableSubscriptionKey),
        state2 = rememberSubscription(screenContext.favoriteTimetableIdsSubscriptionKey),
        errorFallback = { }
    ) { timetable, favoriteTimetableItemIds ->
        val eventFlow = rememberEventFlow<FavoritesScreenEvent>()

        val uiState = favoritesScreenPresenter(
            eventFlow = eventFlow,
            timetable = timetable.copy(bookmarks = favoriteTimetableItemIds),
        )

        FavoritesScreen(
            uiState = uiState,
            onBookmarkClick = { eventFlow.tryEmit(FavoritesScreenEvent.Bookmark(it)) },
            onAllFilterChipClick = { eventFlow.tryEmit(FavoritesScreenEvent.FilterAllDays) },
            onDay1FilterChipClick = { eventFlow.tryEmit(FavoritesScreenEvent.FilterDay1) },
            onDay2FilterChipClick = { eventFlow.tryEmit(FavoritesScreenEvent.FilterDay2) },
        )
    }
}
