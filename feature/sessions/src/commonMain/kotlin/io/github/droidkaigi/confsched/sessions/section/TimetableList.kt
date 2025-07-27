package io.github.droidkaigi.confsched.sessions.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
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
import io.github.droidkaigi.confsched.droidkaigiui.KaigiWindowSizeClassConstants
import io.github.droidkaigi.confsched.droidkaigiui.session.TimetableItemCard
import io.github.droidkaigi.confsched.model.sessions.Timetable
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.droidkaigi.confsched.model.sessions.fake
import io.github.droidkaigi.confsched.sessions.components.TimetableTimeSlot
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

    BoxWithConstraints {
        val isWideScreen = maxWidth >= KaigiWindowSizeClassConstants.WindowWidthSizeClassMediumMinWidth
        val columnCount = if (isWideScreen) 2 else 1

        LazyColumn(
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = contentPadding,
            modifier = modifier,
        ) {
            itemsIndexed(
                items = uiState.timetableItemMap.toList(),
                key = { _, (timeSlot, _) -> timeSlot.key },
            ) { index, (timeSlot, timetableItems) ->
                var timetableTimeSlotHeight by remember { mutableIntStateOf(0) }
                val timetableTimeSlotOffsetY by remember {
                    derivedStateOf {
                        val itemInfo = lazyListState.layoutInfo.visibleItemsInfo.find { it.index == index }
                        // If the item is not visible, keep the TimetableTimeSlot in its original position.
                        if (itemInfo == null) return@derivedStateOf 0

                        val itemTopOffset = itemInfo.offset
                        // A positive offset means the top of the item is within the visible viewport.
                        if (itemTopOffset > 0) return@derivedStateOf 0

                        // Apply a vertical offset to TimetableTimeSlot to create a "sticky" effect while scrolling,
                        // but clamp it to ensure it doesn't overflow beyond the bottom edge of its item.
                        (-itemTopOffset).coerceAtMost(itemInfo.size - timetableTimeSlotHeight)
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    TimetableTimeSlot(
                        startTimeText = timeSlot.startTimeString,
                        endTimeText = timeSlot.endTimeString,
                        modifier = Modifier
                            .onSizeChanged { timetableTimeSlotHeight = it.height }
                            .graphicsLayer { translationY = timetableTimeSlotOffsetY.toFloat() }
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        timetableItems.windowed(columnCount, columnCount, true).forEach { windowedItems ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.height(IntrinsicSize.Max),
                            ) {
                                windowedItems.forEach { item ->
                                    TimetableItemCard(
                                        timetableItem = item,
                                        isBookmarked = uiState.timetable.bookmarks.contains(item.id),
                                        highlightWord = highlightWord,
                                        onBookmarkClick = onBookmarkClick,
                                        onTimetableItemClick = onTimetableItemClick,
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                    )
                                }
                                if (windowedItems.size < columnCount) {
                                    repeat(columnCount - windowedItems.size) {
                                        Spacer(Modifier.weight(1f))
                                    }
                                }
                            }
                        }
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
