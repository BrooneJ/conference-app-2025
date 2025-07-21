package io.github.droidkaigi.confsched.sessions.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.droidkaigiui.KaigiPreviewContainer
import io.github.droidkaigi.confsched.droidkaigiui.session.TimetableItemCard
import io.github.droidkaigi.confsched.model.sessions.Timetable
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.droidkaigi.confsched.model.sessions.fake
import io.github.droidkaigi.confsched.sessions.components.TimetableTime
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun TimetableList(
    uiState: TimetableListUiState,
    onTimetableItemClick: (TimetableItem) -> Unit,
    onBookmarkClick: (TimetableItem, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    highlightWord: String = "",
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        itemsIndexed(items = uiState.timetableItemMap.toList()) { index, (timeSlot, timetableItems) ->
            var timetableTimeHeight by remember { mutableIntStateOf(0) }
            val timetableTimeOffsetY by remember {
                derivedStateOf {
                    val itemInfo = lazyListState.layoutInfo.visibleItemsInfo.find { it.index == index } ?: return@derivedStateOf 0
                    val itemTopOffset = itemInfo.offset
                    if (itemTopOffset > 0) return@derivedStateOf 0
                    (-itemTopOffset).coerceAtMost(itemInfo.size - timetableTimeHeight)
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                TimetableTime(
                    startTimeText = timeSlot.startTimeString,
                    endTimeText = timeSlot.endTimeString,
                    modifier = Modifier
                        .onSizeChanged { timetableTimeHeight = it.height }
                        .graphicsLayer { translationY = timetableTimeOffsetY.toFloat() }
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    timetableItems.forEach { item ->
                        TimetableItemCard(
                            timetableItem = item,
                            isBookmarked = uiState.timetable.bookmarks.contains(item.id),
                            highlightWord = highlightWord,
                            onBookmarkClick = onBookmarkClick,
                            onTimetableItemClick = onTimetableItemClick,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TimetableListPreview() {
    KaigiPreviewContainer {
        TimetableList(
            uiState = TimetableListUiState(
                timetable = Timetable.fake(),
                timetableItemMap = persistentMapOf(
                    TimetableListUiState.TimeSlot(
                        LocalTime(11, 20, 0, 0), endTime = LocalTime(12, 0, 0, 0)
                    ) to List(2) { TimetableItem.Session.fake() },
                    TimetableListUiState.TimeSlot(
                        LocalTime(12, 0, 0, 0), endTime = LocalTime(13, 0, 0, 0)
                    ) to List(3) { TimetableItem.Session.fake() },
                    TimetableListUiState.TimeSlot(
                        LocalTime(13, 0, 0, 0), endTime = LocalTime(14, 0, 0, 0)
                    ) to List(5) { TimetableItem.Session.fake() },
                    TimetableListUiState.TimeSlot(
                        LocalTime(14, 0, 0, 0), endTime = LocalTime(15, 0, 0, 0)
                    ) to List(5) { TimetableItem.Session.fake() },
                ),
            ),
            onTimetableItemClick = {},
            onBookmarkClick = { _, _ -> },
        )
    }
}
