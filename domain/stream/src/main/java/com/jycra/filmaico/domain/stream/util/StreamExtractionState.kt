package com.jycra.filmaico.domain.stream.util

sealed class StreamExtractionState(val message: String) {

    // Estados Base
    object Idle : StreamExtractionState("Esperando...")
    object Initializing : StreamExtractionState("Inicializando proceso de extracción...")

    // Estados de Caché
    object CheckingCache : StreamExtractionState("Verificando caché local...")
    object CacheHit : StreamExtractionState("Recuperando enlace desde la caché...")
    object CacheBypass : StreamExtractionState("Caché expirada o vacía. Iniciando búsqueda externa...")
    object SavingCache : StreamExtractionState("Guardando nueva URL en caché...")

    // Estados para Direct Stream
    object ResolvingCDN : StreamExtractionState("Resolviendo CDN y generación de Tokens...")
    object FetchingCookies : StreamExtractionState("Buscando Cookies y Headers de sesión...")
    object DecryptingDRM : StreamExtractionState("Negociando llaves de descifrado DRM...")

    // Dentro de StreamExtractionState
    object EvaluatingStaticDRMKeys : StreamExtractionState("Evaluando llaves DRM estáticas...")
    object RequestingRemoteDRMKeys : StreamExtractionState("Solicitando licencias DRM al servidor...")

    // Estados para WebView Scraping
    data class ScrapingWebView(val iframe: String) : StreamExtractionState("Analizando sitio fuente...")
    object AwaitingNetworkTraffic : StreamExtractionState("Interceptando tráfico de red del reproductor...")
    object PreloadingManifest : StreamExtractionState("Precargando manifiesto HLS/DASH en RAM...")

    // Estados Finales
    data class Success(val uri: String) : StreamExtractionState("¡Extracción completada con éxito!")
    data class Error(val errorMsg: String, val cause: Throwable? = null) : StreamExtractionState("Fallo: $errorMsg")

}