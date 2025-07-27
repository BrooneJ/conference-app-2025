package io.github.droidkaigi.confsched.data.sessions

import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import io.github.droidkaigi.confsched.data.DataScope
import io.github.droidkaigi.confsched.data.sessions.response.LocaledResponse
import io.github.droidkaigi.confsched.data.sessions.response.SessionAssetResponse
import io.github.droidkaigi.confsched.data.sessions.response.SessionMessageResponse
import io.github.droidkaigi.confsched.data.sessions.response.SessionsAllResponse
import io.github.droidkaigi.confsched.model.core.MultiLangText
import io.github.droidkaigi.confsched.model.core.RoomType
import io.github.droidkaigi.confsched.model.data.TimetableSubscriptionKey
import io.github.droidkaigi.confsched.model.sessions.Timetable
import io.github.droidkaigi.confsched.model.sessions.TimetableAsset
import io.github.droidkaigi.confsched.model.sessions.TimetableCategory
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import io.github.droidkaigi.confsched.model.sessions.TimetableLanguage
import io.github.droidkaigi.confsched.model.sessions.TimetableRoom
import io.github.droidkaigi.confsched.model.sessions.TimetableSessionType
import io.github.droidkaigi.confsched.model.sessions.TimetableSpeaker
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import soil.query.SubscriptionId
import soil.query.buildSubscriptionKey
import kotlin.time.Instant

@ContributesBinding(DataScope::class, binding = binding<TimetableSubscriptionKey>())
@Inject
public class DefaultTimetableSubscriptionKey(
    private val sessionsApiClient: SessionsApiClient,
    private val dataStore: SessionCacheDataStore,
) : TimetableSubscriptionKey by buildSubscriptionKey(
    id = SubscriptionId("timetable"),
    subscribe = {
        flow {
            dataStore.getCache()?.let {
                emit(it.toTimetable())
            }

            val response = sessionsApiClient.sessionsAllResponse()
            dataStore.save(response)

            emit(response.toTimetable())
        }
    },
)

public fun SessionsAllResponse.toTimetable(): Timetable {
    val timetableContents = this
    val speakerIdToSpeaker: Map<String, TimetableSpeaker> = timetableContents.speakers
        .groupBy { it.id }
        .mapValues { (_, apiSpeakers) ->
            apiSpeakers.map { apiSpeaker ->
                TimetableSpeaker(
                    id = apiSpeaker.id,
                    name = apiSpeaker.fullName,
                    bio = apiSpeaker.bio ?: "",
                    iconUrl = apiSpeaker.profilePicture.orEmpty(),
                    tagLine = apiSpeaker.tagLine ?: "",
                )
            }.first()
        }
    val categoryIdToCategory: Map<Int, TimetableCategory> = timetableContents.categories
        .flatMap { it.items }
        .groupBy { it.id }
        .mapValues { (_, apiCategories) ->
            apiCategories.map { apiCategory ->
                TimetableCategory(
                    id = apiCategory.id,
                    title = apiCategory.name.toMultiLangText(),
                )
            }.first()
        }
    val roomIdToRoom: Map<Int, TimetableRoom> = timetableContents.rooms
        .associateBy(
            keySelector = { room -> room.id },
            valueTransform = { room ->
                TimetableRoom(
                    id = room.id,
                    name = room.name.toMultiLangText(),
                    type = room.name.toRoomType(),
                    sort = room.sort,
                )
            },
        )

    return Timetable(
        timetableItems = timetableContents.sessions.map { apiSession ->
            if (!apiSession.isServiceSession) {
                TimetableItem.Session(
                    id = TimetableItemId(apiSession.id),
                    title = apiSession.title.toMultiLangText(),
                    startsAt = apiSession.startsAt.toInstantAsJST(),
                    endsAt = apiSession.endsAt.toInstantAsJST(),
                    category = categoryIdToCategory[apiSession.sessionCategoryItemId]!!,
                    sessionType = TimetableSessionType.ofOrNull(apiSession.sessionType)!!,
                    room = roomIdToRoom[apiSession.roomId]!!,
                    targetAudience = apiSession.targetAudience,
                    language = TimetableLanguage(
                        langOfSpeaker = apiSession.language,
                        isInterpretationTarget = apiSession.interpretationTarget,
                    ),
                    asset = apiSession.asset.toTimetableAsset(),
                    description = if (
                        apiSession.i18nDesc?.ja == null &&
                        apiSession.i18nDesc?.en == null
                    ) {
                        MultiLangText(
                            jaTitle = apiSession.description ?: "",
                            enTitle = apiSession.description ?: "",
                        )
                    } else {
                        apiSession.i18nDesc.toMultiLangText()
                    },
                    speakers = apiSession.speakers
                        .map { speakerIdToSpeaker[it]!! }
                        .toPersistentList(),
                    message = apiSession.message?.toMultiLangText(),
                    levels = apiSession.levels.toPersistentList(),
                )
            } else {
                TimetableItem.Special(
                    id = TimetableItemId(apiSession.id),
                    title = apiSession.title.toMultiLangText(),
                    startsAt = apiSession.startsAt.toInstantAsJST(),
                    endsAt = apiSession.endsAt.toInstantAsJST(),
                    category = categoryIdToCategory[apiSession.sessionCategoryItemId]!!,
                    sessionType = TimetableSessionType.ofOrNull(apiSession.sessionType)!!,
                    room = roomIdToRoom[apiSession.roomId]!!,
                    targetAudience = apiSession.targetAudience,
                    language = TimetableLanguage(
                        langOfSpeaker = apiSession.language,
                        isInterpretationTarget = apiSession.interpretationTarget,
                    ),
                    asset = apiSession.asset.toTimetableAsset(),
                    speakers = apiSession.speakers
                        .map { speakerIdToSpeaker[it]!! }
                        .toPersistentList(),
                    levels = apiSession.levels.toPersistentList(),
                    description = if (
                        apiSession.i18nDesc?.ja == null &&
                        apiSession.i18nDesc?.en == null
                    ) {
                        MultiLangText(
                            jaTitle = apiSession.description ?: "",
                            enTitle = apiSession.description ?: "",
                        )
                    } else {
                        apiSession.i18nDesc.toMultiLangText()
                    },
                    message = apiSession.message?.toMultiLangText(),
                )
            }
        }
            .sortedWith(
                compareBy<TimetableItem> { it.startsAt }
                    .thenBy { it.room },
            )
            .toPersistentList(),
    )
}

private fun LocaledResponse.toMultiLangText() =
    MultiLangText(jaTitle = ja ?: "", enTitle = en ?: "")

private fun SessionMessageResponse.toMultiLangText() = MultiLangText(jaTitle = ja, enTitle = en)

private fun SessionAssetResponse.toTimetableAsset() = TimetableAsset(videoUrl, slideUrl)

internal fun String.toInstantAsJST(): Instant {
    val (date, _) = split("+")
    return LocalDateTime.parse(date).toInstant(TimeZone.of("UTC+9"))
}

private fun LocaledResponse.toRoomType() = when (en?.lowercase()) {
    "flamingo" -> RoomType.RoomF
    "giraffe" -> RoomType.RoomG
    "hedgehog" -> RoomType.RoomH
    "iguana" -> RoomType.RoomI
    "jellyfish" -> RoomType.RoomJ
    // Assume the room on the first day.
    else -> RoomType.RoomIJ
}
