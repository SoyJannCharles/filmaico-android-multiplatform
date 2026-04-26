package com.jycra.filmaico.core.network.di

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jycra.filmaico.core.config.ConfigSource
import com.jycra.filmaico.core.network.NetworkConnectivityObserver
import com.jycra.filmaico.core.network.StreamNetworkSource
import com.jycra.filmaico.core.network.api.StreamApi
import com.jycra.filmaico.core.network.cookies.AppCookieJar
import com.jycra.filmaico.data.stream.data.service.StreamService
import com.jycra.filmaico.domain.network.ConnectivityObserver
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
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

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
        @XAuthHttpClient
        fun provideXAuthHttpClient(
            loggingInterceptor: HttpLoggingInterceptor,
            @FailFastInterceptor failFastInterceptor: Interceptor,
            @XAuthInterceptor xAuthInterceptor: Interceptor,
            cookieJar: AppCookieJar
        ): OkHttpClient {

            val trustAllCerts = arrayOf<TrustManager>(@SuppressLint("CustomX509TrustManager")
            object : X509TrustManager {
                @SuppressLint("TrustAllX509TrustManager")
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                @SuppressLint("TrustAllX509TrustManager")
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })

            val sslContext = SSLContext.getInstance("SSL").apply {
                init(null, trustAllCerts, SecureRandom())
            }

            return OkHttpClient.Builder()
                .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }
                .cookieJar(cookieJar)
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
        fun provideStreamApiService(retrofit: Retrofit): StreamApi {
            return retrofit.create(StreamApi::class.java)
        }

    }

}