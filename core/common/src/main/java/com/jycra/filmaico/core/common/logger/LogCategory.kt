package com.jycra.filmaico.core.common.logger

enum class LogCategory(val tag: String) {

    DATABASE("FILMAICO_DATABASE"),
    API("FILMAICO_API"),
    NETWORK("FILMAICO_NET"),

    ROOM("FILMAICO_ROOM"),
    DATASTORE("FILMAICO_DATASTORE"),

    PLAYER("FILMAICO_PLAYER"),
    DRM("FILMAICO_DRM"),
    SCRAPER("FILMAICO_SCRAPE"),
    UI("FILMAICO_UI")

}