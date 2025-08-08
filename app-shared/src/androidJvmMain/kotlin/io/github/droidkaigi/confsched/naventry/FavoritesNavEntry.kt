package io.github.droidkaigi.confsched.naventry

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import io.github.droidkaigi.confsched.AppGraph
import io.github.droidkaigi.confsched.favorites.FavoritesScreenRoot
import io.github.droidkaigi.confsched.favorites.rememberFavoritesScreenContextRetained
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import io.github.droidkaigi.confsched.navkey.FavoritesNavKey

context(appGraph: AppGraph)
fun EntryProviderBuilder<NavKey>.favoritesEntry(
    onTimetableItemClick: (TimetableItemId) -> Unit
) {
    entry<FavoritesNavKey> {
        with(appGraph.rememberFavoritesScreenContextRetained()) {
            FavoritesScreenRoot(
                onTimetableItemClick = onTimetableItemClick,
            )
        }
    }
}
