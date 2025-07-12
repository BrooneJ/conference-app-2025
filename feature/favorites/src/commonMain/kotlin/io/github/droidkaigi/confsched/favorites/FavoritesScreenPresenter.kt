package io.github.droidkaigi.confsched.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import io.github.droidkaigi.confsched.common.compose.EventEffect
import io.github.droidkaigi.confsched.common.compose.EventFlow
import io.github.droidkaigi.confsched.common.compose.providePresenterDefaults
import io.github.droidkaigi.confsched.model.core.DroidKaigi2025Day
import io.github.droidkaigi.confsched.model.core.Filters
import io.github.droidkaigi.confsched.model.sessions.Timetable
import io.github.takahirom.rin.rememberRetained
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap
import soil.query.compose.rememberMutation

private const val MIN_FILTERS_TO_DESELECT = 2

context(screenContext: FavoritesScreenContext)
@Composable
fun favoritesScreenPresenter(
    eventFlow: EventFlow<FavoritesScreenEvent>,
    timetable: Timetable,
): FavoritesScreenUiState = providePresenterDefaults {
    val favoriteTimetableItemIdMutation = rememberMutation(screenContext.favoriteTimetableItemIdMutationKey)

    var allFilterSelected by rememberRetained { mutableStateOf(true) }
    var selectedDayFilters by rememberRetained { mutableStateOf(emptySet<DroidKaigi2025Day>()) }

    EventEffect(eventFlow) { event ->
        when (event) {
            is FavoritesScreenEvent.Bookmark -> {
                favoriteTimetableItemIdMutation.mutate(event.id)
            }

            FavoritesScreenEvent.FilterAllDays -> {
                allFilterSelected = true
                selectedDayFilters = emptySet()
            }

            FavoritesScreenEvent.FilterDay1,
            FavoritesScreenEvent.FilterDay2 -> {
                allFilterSelected = false

                val dayType = if (event is FavoritesScreenEvent.FilterDay1) {
                    DroidKaigi2025Day.ConferenceDay1
                } else {
                    DroidKaigi2025Day.ConferenceDay2
                }

                selectedDayFilters =
                    if (selectedDayFilters.contains(dayType) && selectedDayFilters.size >= MIN_FILTERS_TO_DESELECT) {
                        selectedDayFilters - dayType
                    } else {
                        selectedDayFilters + dayType
                    }
            }
        }
    }

    favoritesScreenUiState(
        timetable = timetable,
        allFilterSelected = allFilterSelected,
        selectedDayFilters = selectedDayFilters
    )
}

@Composable
private fun favoritesScreenUiState(
    timetable: Timetable,
    allFilterSelected: Boolean,
    selectedDayFilters: Set<DroidKaigi2025Day>,
): FavoritesScreenUiState {
    val filteredSessions by rememberUpdatedState(
        timetable
            .filtered(
                Filters(
                    filterFavorite = true,
                    days = selectedDayFilters.toList(),
                ),
            )
            .timetableItems.groupBy {
                FavoritesScreenUiState.TimetableContentState.FavoriteList.TimeSlot(
                    it.day, it.startsTimeString, it.endsTimeString
                )
            }.mapValues { entry ->
                entry.value.sortedWith(
                    compareBy({ it.day?.name.orEmpty() }, { it.startsTimeString }),
                )
            }.toPersistentMap(),
    )

    return FavoritesScreenUiState(
        filterState = FavoritesScreenUiState.FilterState(
            currentDayFilter = selectedDayFilters.toPersistentList(),
            allFilterSelected = allFilterSelected
        ),
        timetableContentState = if (filteredSessions.isEmpty()) {
            FavoritesScreenUiState.TimetableContentState.Empty
        } else {
            FavoritesScreenUiState.TimetableContentState.FavoriteList(
                timetableItemMap = filteredSessions,
            )
        }
    )
}
