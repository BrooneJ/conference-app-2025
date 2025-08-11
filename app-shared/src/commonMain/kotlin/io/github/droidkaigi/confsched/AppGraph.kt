package io.github.droidkaigi.confsched

import io.github.confsched.profile.ProfileScreenContext
import io.github.droidkaigi.confsched.about.AboutScreenContext
import io.github.droidkaigi.confsched.contributors.ContributorsScreenContext
import io.github.droidkaigi.confsched.favorites.FavoritesScreenContext
import io.github.droidkaigi.confsched.sessions.SearchScreenContext
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailScreenContext
import io.github.droidkaigi.confsched.sessions.TimetableScreenContext
import io.github.droidkaigi.confsched.sponsors.SponsorsScreenContext
import io.github.droidkaigi.confsched.staff.StaffScreenContext

interface AppGraph :
    TimetableScreenContext.Factory,
    TimetableItemDetailScreenContext.Factory,
    SearchScreenContext.Factory,
    ContributorsScreenContext.Factory,
    FavoritesScreenContext.Factory,
    AboutScreenContext.Factory,
    SponsorsScreenContext.Factory,
    StaffScreenContext.Factory,
    ProfileScreenContext.Factory
