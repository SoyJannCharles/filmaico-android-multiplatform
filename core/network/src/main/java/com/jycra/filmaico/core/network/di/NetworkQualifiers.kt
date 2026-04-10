package com.jycra.filmaico.core.network.di

import jakarta.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class XAuthHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FailFastInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class XAuthInterceptor