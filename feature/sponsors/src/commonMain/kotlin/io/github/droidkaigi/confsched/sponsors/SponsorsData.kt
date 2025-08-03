package io.github.droidkaigi.confsched.sponsors

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

data class Sponsor (
    val name: String,
    val logo: String,
    val plan: Plan,
    val link: String,
) {
    public companion object
}

public enum class Plan {
    PLATINUM,
    GOLD,
    SUPPORTER,
    ;

    public val isSupporter: Boolean
        get() = this == SUPPORTER
    public val isPlatinum: Boolean
        get() = this == PLATINUM
    public val isGold: Boolean
        get() = this == GOLD

    public companion object {
        public fun ofOrNull(plan: String): Plan? {
            return entries.firstOrNull { it.name == plan }
        }
    }
}

data class SponsorList (
    val platinumSponsors: List<Sponsor>,
    val goldSponsors: List<Sponsor>,
    val supporters: List<Sponsor>
)

public fun Sponsor.Companion.fakes(): PersistentList<Sponsor> = (
        List(3) {
            Sponsor(
                name = "DroidKaigi PLATINUM Section $it",
                logo = "https://placehold.jp/150x150.png",
                plan = Plan.PLATINUM,
                link = "https://developer.android.com/",
            )
        } + List(5) {
            Sponsor(
                name = "DroidKaigi GOLD Section $it",
                logo = "https://placehold.jp/150x150.png",
                plan = Plan.GOLD,
                link = "https://developer.android.com/",
            )
        } + List(12) {
            Sponsor(
                name = "DroidKaigi Supporter Section $it",
                logo = "https://placehold.jp/150x150.png",
                plan = Plan.SUPPORTER,
                link = "https://developer.android.com/",
            )
        }
        ).toPersistentList()
