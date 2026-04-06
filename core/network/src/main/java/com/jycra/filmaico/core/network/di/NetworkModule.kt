package com.jycra.filmaico.core.network.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jycra.filmaico.core.config.ConfigSource
import com.jycra.filmaico.core.network.ConnectivityObserver
import com.jycra.filmaico.core.network.NetworkConnectivityObserver
import com.jycra.filmaico.core.network.StreamNetworkSource
import com.jycra.filmaico.core.network.api.StreamApi
import com.jycra.filmaico.core.network.cookies.AppCookieJar
import com.jycra.filmaico.data.stream.data.service.StreamService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindStreamService(
        impl: StreamNetworkSource
    ): StreamService

    @Binds
    @Singleton
    abstract fun bindConnectivityObserver(
        impl: NetworkConnectivityObserver
    ): ConnectivityObserver

    companion object {

        @Provides
        @Singleton
        fun provideGson(): Gson = GsonBuilder().create()

        @Provides
        @Singleton
        @AuthHttpClient
        fun provideAuthHttpClient(
            loggingInterceptor: HttpLoggingInterceptor,
            authInterceptor: Interceptor,
            cookieJar: AppCookieJar
        ): OkHttpClient {
            return OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()
        }

        @Provides
        @Singleton
        fun provideLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY // Para ver todo el detalle en debug
            }
        }

        @Provides
        @Singleton
        fun provideAuthInterceptor(configSource: ConfigSource): Interceptor {
            return Interceptor { chain ->
                val request = chain.request()
                val requestBuilder = request.newBuilder()
                if (request.url.host.endsWith("cdn.tvar.io")) {
                    requestBuilder.header("xauthorization", configSource.getTvarCdnAuthHeader())
                }
                chain.proceed(requestBuilder.build())
            }
        }

        @Provides
        @Singleton
        fun provideRetrofit(
            @AuthHttpClient authHttpClient: OkHttpClient,
            gson: Gson
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://placeholder.com/")
                .client(authHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        @Provides
        @Singleton
        fun provideStreamApiService(retrofit: Retrofit): StreamApi {
            return retrofit.create(StreamApi::class.java)
        }

    }

}