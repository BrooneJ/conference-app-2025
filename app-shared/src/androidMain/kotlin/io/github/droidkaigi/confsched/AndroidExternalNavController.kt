package io.github.droidkaigi.confsched

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.CalendarContract
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.annotation.RequiresApi
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import io.github.droidkaigi.confsched.model.sessions.TimetableItem

class AndroidExternalNavController(
    private val context: Context,
) : ExternalNavController {

    override fun navigate(url: String) {
        val uri: Uri = url.toUri()
        val nativeAppLaunched = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            navigateToNativeAppApi30(context = context, uri = uri)
        } else {
            navigateToNativeApp(context = context, uri = uri)
        }
        if (nativeAppLaunched) return

        navigateToCustomTab(context = context, uri = uri)
    }

    override fun navigateToCalendarRegistration(timetableItem: TimetableItem) {
        val calendarIntent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtras(
                bundleOf(
                    CalendarContract.EXTRA_EVENT_BEGIN_TIME to timetableItem.startsAt.toEpochMilliseconds(),
                    CalendarContract.EXTRA_EVENT_END_TIME to timetableItem.endsAt.toEpochMilliseconds(),
                    CalendarContract.Events.TITLE to "[${timetableItem.room.name.currentLangTitle}] ${timetableItem.title.currentLangTitle}",
                    CalendarContract.Events.DESCRIPTION to timetableItem.url,
                    CalendarContract.Events.EVENT_LOCATION to timetableItem.room.name.currentLangTitle,
                ),
            )
        }

        runCatching {
            context.startActivity(calendarIntent)
        }.onFailure {
            Log.e("ExternalNavController", "Fail startActivity in navigateToCalendarRegistration", it)
        }
    }

    override fun onShareClick(timetableItem: TimetableItem) {
        val text = "[${timetableItem.room.name.currentLangTitle}] ${timetableItem.formattedMonthAndDayString} " +
                "${timetableItem.startsTimeString} - ${timetableItem.endsTimeString}\n" +
                "${timetableItem.title.currentLangTitle}\n" +
                timetableItem.url
        try {
            ShareCompat.IntentBuilder(context)
                .setText(text)
                .setType("text/plain")
                .startChooser()
        } catch (e: ActivityNotFoundException) {
            Log.e("ExternalNavController", "ActivityNotFoundException Fail startActivity", e)
        }
    }

    @Suppress("SwallowedException")
    @RequiresApi(Build.VERSION_CODES.R)
    private fun navigateToNativeAppApi30(
        context: Context,
        uri: Uri,
    ): Boolean {
        val nativeAppIntent =
            Intent(Intent.ACTION_VIEW, uri)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER)
        return try {
            context.startActivity(nativeAppIntent)
            true
        } catch (e: ActivityNotFoundException) {
            Log.w("ExternalNavController", "ActivityNotFoundException in navigateToNativeAppApi30", e)
            false
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun navigateToNativeApp(
        context: Context,
        uri: Uri,
    ): Boolean {
        val pm = context.packageManager

        // Get all apps that resolve the specific Url
        val specializedActivityIntent = Intent(Intent.ACTION_VIEW, uri)
            .addCategory(Intent.CATEGORY_BROWSABLE)
        val resolvedSpecializedList: MutableSet<String> =
            pm.queryIntentActivities(specializedActivityIntent, 0)
                .map { it.activityInfo.packageName }
                .toMutableSet()

        // Get all Apps that resolve a generic url
        val browserActivityIntent = Intent()
            .setAction(Intent.ACTION_VIEW)
            .addCategory(Intent.CATEGORY_BROWSABLE)
            .setData(Uri.fromParts("http", "", null))
        val genericResolvedList: Set<String> =
            pm.queryIntentActivities(browserActivityIntent, 0)
                .map { it.activityInfo.packageName }
                .toSet()

        // Keep only the Urls that resolve the specific, but not the generic urls.
        resolvedSpecializedList.removeAll(genericResolvedList)

        // If the list is empty, no native app handlers were found.
        if (resolvedSpecializedList.isEmpty()) {
            return false
        }

        // We found native handlers. Launch the Intent.
        specializedActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(specializedActivityIntent)
        return true
    }

    @Suppress("SwallowedException")
    private fun navigateToCustomTab(
        context: Context,
        uri: Uri,
    ): Boolean {
        return try {
            CustomTabsIntent.Builder()
                .setShowTitle(true)
                .build()
                .launchUrl(context, uri)
            true
        } catch (e: ActivityNotFoundException) {
            Log.e("ExternalNavController", "ActivityNotFoundException in navigateToCustomTab", e)
            false
        }
    }
}
