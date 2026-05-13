package com.jycra.filmaico.core.network.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jycra.filmaico.core.config.ConfigSource
import com.jycra.filmaico.core.network.EpgSource
import com.jycra.filmaico.core.network.api.EdgeNodeApi
import com.jycra.filmaico.core.network.api.EpgApi
import com.jycra.filmaico.core.network.api.StreamCredentialsApi
import com.jycra.filmaico.core.network.cookies.AppCookieJar
import com.jycra.filmaico.core.network.observer.NetworkConnectivityObserver
import com.jycra.filmaico.core.network.provider.RemoteCookieProvider
import com.jycra.filmaico.core.network.provider.RemoteDrmKeyProvider
import com.jycra.filmaico.core.network.provider.RemoteEdgeNodeProvider
import com.jycra.filmaico.core.network.resolver.DefaultIframeResolver
import com.jycra.filmaico.core.network.util.prober.EdgeNodeProberImpl
import com.jycra.filmaico.core.network.util.prober.SeedProberImpl
import com.jycra.filmaico.data.media.data.service.EpgService
import com.jycra.filmaico.data.stream.data.provider.CookieProvider
import com.jycra.filmaico.data.stream.data.provider.DrmKeyProvider
import com.jycra.filmaico.data.stream.data.provider.EdgeNodeProvider
import com.jycra.filmaico.data.stream.resolver.IframeResolver
import com.jycra.filmaico.data.stream.util.prober.EdgeNodeProber
import com.jycra.filmaico.data.stream.util.prober.SeedProber
import com.jycra.filmaico.domain.network.ConnectivityObserver
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindIframeResolver(
        impl: DefaultIframeResolver
    ): IframeResolver

    @Binds
    @Singleton
    abstract fun bindEdgeNodeProvider(
        impl: RemoteEdgeNodeProvider
    ): EdgeNodeProvider

    @Binds
    @Singleton
    abstract fun bindEpgService(
        impl: EpgSource
    ): EpgService

    @Binds
    @Singleton
    abstract fun bindEdgeProber(
        impl: EdgeNodeProberImpl
    ): EdgeNodeProber

    @Binds
    @Singleton
    abstract fun bindStreamProber(
        impl: SeedProberImpl
    ): SeedProber

    @Binds
    @Singleton
    abstract fun bindCookieProvider(
        impl: RemoteCookieProvider
    ): CookieProvider

    @Binds
    @Singleton
    abstract fun bindDrmKeyProvider(
        impl: RemoteDrmKeyProvider
    ): DrmKeyProvider

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
        fun provideHttpClient(): OkHttpClient {
            return OkHttpClient.Builder().build()
        }

        @Provides
        @Singleton
        @ProbeHttpClient
        fun provideProbeOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(1500, TimeUnit.MILLISECONDS)
                .readTimeout(1500, TimeUnit.MILLISECONDS)
                .callTimeout(2000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(false)
                .build()
        }

        @Provides
        @Singleton
        @XAuthHttpClient
        fun provideXAuthHttpClient(
            loggingInterceptor: HttpLoggingInterceptor,
            @FailFastInterceptor failFastInterceptor: Interceptor,
            @XAuthInterceptor xAuthInterceptor: Interceptor,
            cookieJar: AppCookieJar
        ): OkHttpClient {

            return OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .protocols(listOf(Protocol.HTTP_2, Protocol.HTTP_1_1))
                .connectionPool(ConnectionPool(20, 1, TimeUnit.MINUTES))
                .retryOnConnectionFailure(true)
                .addInterceptor(failFastInterceptor)
                .addInterceptor(xAuthInterceptor)
                .addInterceptor(loggingInterceptor)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()

        }

        @Provides
        @Singleton
        @FailFastInterceptor
        fun provideFailFastInterceptor(): Interceptor {
            return Interceptor { chain ->

                val response = chain.proceed(chain.request())
                val code = response.code

                if (code < 400 || code > 503) {
                    return@Interceptor response
                }

                response.close()
                throw IOException("HTTP $code – ${chain.request().url}")

            }
        }

        @Provides
        @Singleton
        @XAuthInterceptor
        fun provideXAuthInterceptor(configSource: ConfigSource): Interceptor {
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
        fun provideLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }

        @Provides
        @Singleton
        fun provideRetrofit(
            @XAuthHttpClient xAuthClient: OkHttpClient,
            gson: Gson
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://placeholder.com/")
                .client(xAuthClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        @Provides
        @Singleton
        fun provideStreamCredentialsApi(retrofit: Retrofit): StreamCredentialsApi {
            return retrofit.create(StreamCredentialsApi::class.java)
        }

        @Provides
        @Singleton
        fun provideEdgeNodeApi(retrofit: Retrofit): EdgeNodeApi {
            return retrofit.create(EdgeNodeApi::class.java)
        }

        @Provides
        @Singleton
        fun provideEpgApi(retrofit: Retrofit): EpgApi {
            return retrofit.create(EpgApi::class.java)
        }

    }

}