package io.github.droidkaigi.confsched.data.user

import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.data.DataScope
import io.github.droidkaigi.confsched.model.data.FavoriteTimetableIdsSubscriptionKey
import soil.query.SubscriptionId
import soil.query.buildSubscriptionKey

@ContributesBinding(DataScope::class)
@Inject
public class DefaultFavoriteTimetableIdsSubscriptionKey(
    private val userDataStore: UserDataStore,
) : FavoriteTimetableIdsSubscriptionKey by buildSubscriptionKey(
    id = SubscriptionId("favorites"),
    subscribe = { userDataStore.getStream() },
)
