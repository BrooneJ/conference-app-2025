package io.github.droidkaigi.confsched.sessions.section

import io.github.droidkaigi.confsched.droidkaigiui.session.TimeSlotItem
import io.github.droidkaigi.confsched.model.sessions.Timetable
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import kotlinx.collections.immutable.PersistentMap
import kotlinx.datetime.LocalTime

data class TimetableListUiState(
    val timetableItemMap: PersistentMap<TimeSlot, List<TimetableItem>>,
    val timetable: Timetable,
) {
    data class TimeSlot(
        val startTime: LocalTime,
        val endTime: LocalTime,
    ) : TimeSlotItem {
        override val startTimeString: String get() = startTime.toTimetableTimeString()
        override val endTimeString: String get() = endTime.toTimetableTimeString()

        override val key: String get() = "$startTime-$endTime"

        private fun LocalTime.toTimetableTimeString(): String {
            return "$hour".padStart(2, '0') + ":" + "$minute".padStart(2, '0')
        }
    }
}
