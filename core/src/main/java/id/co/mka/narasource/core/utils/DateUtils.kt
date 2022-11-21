package id.co.mka.narasource.core.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private const val SECOND_MILLIS = 1000
    private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS = 24 * HOUR_MILLIS

    fun formatTimeStamp(timeStamp: String): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        format.timeZone = TimeZone.getTimeZone("UTC")
        val date = format.parse(timeStamp) as Date
        if (date.time < 1000000000000L) {
            date.time *= 1000
        }
        val now = System.currentTimeMillis()
        if (date.time > now || date.time <= 0) {
            return ""
        }
        val diff = now - date.time
        return if (diff < MINUTE_MILLIS) {
            "Baru saja"
        } else if (diff < 2 * MINUTE_MILLIS) {
            "1 menit yang lalu"
        } else if (diff < 50 * MINUTE_MILLIS) {
            (diff / MINUTE_MILLIS).toString() + " menit yang lalu"
        } else if (diff < 90 * MINUTE_MILLIS) {
            "1 jam yang lalu"
        } else if (diff < 24 * HOUR_MILLIS) {
            (diff / HOUR_MILLIS).toString() + " jam yang lalu"
        } else if (diff < 48 * HOUR_MILLIS) {
            "Kemarin"
        } else if (diff < 72 * HOUR_MILLIS) {
            (diff / DAY_MILLIS).toString() + " hari yang lalu"
        } else {
            return DateFormat.getDateInstance(DateFormat.FULL, Locale("in", "ID")).format(date)
        }
    }

    fun formatTimeStampToDate(timeStamp: String): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        val formatterTime = SimpleDateFormat("HH:mm", Locale("in", "ID"))
        val formatterDate = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("in", "ID"))

        format.timeZone = TimeZone.getTimeZone("UTC")
        val date = format.parse(timeStamp) as Date
        if (date.time < 1000000000000L) {
            date.time *= 1000
        }
        val clockTime = formatterTime.format(date)
        val fullDate = formatterDate.format(date)

        val now = System.currentTimeMillis()
        val diff = now - date.time
        return if (diff < 24 * HOUR_MILLIS) {
            "Hari ini, $clockTime"
        } else if (diff < 48 * HOUR_MILLIS) {
            "Kemarin, $clockTime"
        } else {
            return fullDate.toString()
        }
    }
}
