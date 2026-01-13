package com.jycra.filmaico.feature.main.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jycra.filmaico.core.navigation.route.MainRoutes
import com.jycra.filmaico.core.ui.R

enum class MobileNavItems(
    val route: String,
    @StringRes val titleResId: Int,
    @DrawableRes val iconResId: Int
) {

    HOME(
        route = MainRoutes.HOME,
        titleResId = R.string.bottomnav_item_home,
        iconResId = R.drawable.ic_home
    ),
    SEARCH(
        route = MainRoutes.SEARCH,
        titleResId = R.string.bottomnav_item_search,
        iconResId = R.drawable.ic_search
    ),
    MY_LIST(
        route = MainRoutes.MY_LIST,
        titleResId = R.string.bottomnav_item_mylist,
        iconResId = R.drawable.ic_favorite_mark
    ),
    PROFILE(
        route = MainRoutes.PROFILE,
        titleResId = R.string.bottomnav_item_profile,
        iconResId = R.drawable.ic_account
    )

}