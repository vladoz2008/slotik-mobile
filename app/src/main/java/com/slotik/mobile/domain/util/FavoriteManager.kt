package com.slotik.mobile.domain.util

object FavoriteManager {
    fun toggle(favorites: Set<String>, specialistId: String): Set<String> =
        if (specialistId in favorites) favorites - specialistId else favorites + specialistId

    fun isFavorite(favorites: Set<String>, specialistId: String): Boolean =
        specialistId in favorites
}
