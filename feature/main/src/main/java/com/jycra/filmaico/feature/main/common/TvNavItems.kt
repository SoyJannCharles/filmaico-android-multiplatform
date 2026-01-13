package com.jycra.filmaico.feature.main.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jycra.filmaico.core.navigation.route.MainRoutes
import com.jycra.filmaico.core.ui.R

enum class TvNavItems(
    val route: String,
    @StringRes val titleResId: Int,
    @DrawableRes val iconResId: Int
) {

    SEARCH(
        route = MainRoutes.SEARCH,
        titleResId = R.string.railnav_item_search,
        iconResId = R.drawable.ic_search
    ),
    HOME(
        route = MainRoutes.HOME,
        titleResId = R.string.railnav_item_home,
        iconResId = R.drawable.ic_home
    ),
    MY_LIST(
        route = MainRoutes.MY_LIST,
        titleResId = R.string.railnav_item_mylist,
        iconResId = R.drawable.ic_favorite_mark
    ),
    MOVIES(
        route = MainRoutes.MOVIES,
        titleResId = R.string.railnav_item_movies,
        iconResId = R.drawable.ic_movie
    ),
    SERIES(
        route = MainRoutes.SERIES,
        titleResId = R.string.railnav_item_series,
        iconResId = R.drawable.ic_serie
    ),
    CHANNELS(
        route = MainRoutes.CHANNELS,
        titleResId = R.string.railnav_item_channels,
        iconResId = R.drawable.ic_tv
    ),
    ANIMES(
        route = MainRoutes.ANIME,
        titleResId = R.string.railnav_item_animes,
        iconResId = R.drawable.ic_anime
    ),
    PROFILE(
        route = MainRoutes.PROFILE,
        titleResId = R.string.railnav_item_profile,
        iconResId = R.drawable.ic_account
    )

}