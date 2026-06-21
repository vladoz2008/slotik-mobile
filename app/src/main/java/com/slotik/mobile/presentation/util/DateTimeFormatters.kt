package com.slotik.mobile.presentation.util

import java.time.format.DateTimeFormatter
import java.util.Locale

/** Centralised RU date/time formatters (extracted from the former monolithic SlotikViewModel). */
object DateTimeFormatters {
    private val ruLocale: Locale = Locale.forLanguageTag("ru")

    val dayFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM", ruLocale)
    val shortDayFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMM", ruLocale)
    val weekdayFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE", ruLocale)
    val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm", ruLocale)
}
