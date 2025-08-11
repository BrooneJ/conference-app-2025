package io.github.droidkaigi.confsched.data.profile

import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.data.DataScope
import io.github.droidkaigi.confsched.data.user.UserDataStore
import io.github.droidkaigi.confsched.model.profile.ProfileSubscriptionKey
import soil.query.SubscriptionId
import soil.query.buildSubscriptionKey

@ContributesBinding(DataScope::class)
@Inject
public class DefaultProfileSubscriptionKey(
    private val dataStore: UserDataStore,
) : ProfileSubscriptionKey by buildSubscriptionKey(
    id = SubscriptionId("profile"),
    subscribe = { dataStore.getProfileOrNull() }
)
