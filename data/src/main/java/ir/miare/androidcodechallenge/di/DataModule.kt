package ir.miare.androidcodechallenge.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.logicbase.mockfit.MockFitInterceptor
import ir.miare.androidcodechallenge.local.AppDatabase
import ir.miare.androidcodechallenge.remote.PlayerApi
import ir.miare.androidcodechallenge.repository.PlayerRepository
import ir.miare.androidcodechallenge.repository.PlayerRepositoryImpl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app_db"
        ).build()
    }

    @Provides
    @Singleton
    fun providePlayerApi(@ApplicationContext appContext: Context): PlayerApi {
        // Keep the original mock interceptor config from provided project so retrofit mocks data.json when calling /list
        val client = OkHttpClient.Builder()
            /*.addInterceptor(
                MockFitInterceptor(
                    bodyFactory = { input -> appContext.assets.open(input) },
                    logger = { tag, message -> Log.d(tag, message) },
                    baseUrl = "https://test_baseurl.com/v2/",
                    requestPathToJsonMap  = MockFitConfig.REQUEST_TO_JSON,
                    mockFilesPath = "",
                    mockFitEnable = true,
                    apiEnableMock = true,
                    apiIncludeIntoMock = arrayOf(),
                    apiExcludeFromMock = arrayOf(),
                    apiResponseLatency = 500L
                )
            )*/
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://test_baseurl.com/v2/")
            .addConverterFactory(JacksonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(PlayerApi::class.java)
    }

    @Provides
    @Singleton
    fun providePlayerRepository(api: PlayerApi, db: AppDatabase): PlayerRepository {
        return PlayerRepositoryImpl(api, db)
    }
}