package com.jycra.filmaico.core.navigation.route

object AppRoutes {

    const val SPLASH = "splash"

    const val UPDATE = "update"

    const val SIGN_UP = "signUp"
    const val SIGN_IN = "signIn"

    const val SUBSCRIPTION = "subscription"

    const val MAIN = "main"

    const val DETAIL_WITH_ARGS = "detail/{mediaType}/{containerId}"

    fun detailWithArgs(mediaType: String, containerId: String) =
        "detail/$mediaType/$containerId"

    const val PLAYER_WITH_ARGS = "player/{mediaType}/{assetId}"

    fun playerWithArgs(mediaType: String, assetId: String): String =
        "player/$mediaType/$assetId"

}