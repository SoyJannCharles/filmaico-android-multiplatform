package com.jycra.filmaico.core.navigation.route

object AppRoutes {

    const val SPLASH = "splash"

    const val SIGN_UP = "signUp"
    const val SIGN_IN = "signIn"

    const val PAY = "pay"

    const val MAIN = "main"

    const val MOVIE_DETAIL_WITH_ARGS = "detail/movie/{movieId}"
    const val SERIE_DETAIL_WITH_ARGS = "detail/serie/{serieId}"
    const val ANIME_DETAIL_WITH_ARGS = "detail/anime/{animeId}"

    const val PLAYER_WITH_ARGS = "player/{contentType}/{contentId}"

    fun movieDetailWithArgs(contentId: String) = "detail/movie/$contentId"
    fun serieDetailWithArgs(contentId: String) = "detail/serie/$contentId"
    fun animeDetailWithArgs(contentId: String) = "detail/anime/$contentId"

    fun playerWithArgs(contentType: String, contentId: String) = "player/$contentType/$contentId"

}