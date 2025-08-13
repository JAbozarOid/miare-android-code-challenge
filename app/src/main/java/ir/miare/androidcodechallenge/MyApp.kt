package ir.miare.androidcodechallenge

import android.app.Application
import androidx.room.Room
import ir.miare.androidcodechallenge.local.AppDatabase
import ir.miare.androidcodechallenge.remote.PlayerApi
import ir.miare.androidcodechallenge.repository.PlayerRepository
import ir.miare.androidcodechallenge.repository.PlayerRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

class MyApp : Application() {

    // Safe app-wide scope
    private val appScope by lazy { CoroutineScope(SupervisorJob() + Dispatchers.IO) }

    lateinit var repo: PlayerRepository
        private set

    override fun onCreate() {
        super.onCreate()

        // Build dependencies manually
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_db"
        ).build()

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

        val api = retrofit.create(PlayerApi::class.java)
        repo = PlayerRepositoryImpl(api, db)

        // Seed DB in background
        appScope.launch {
            runCatching {
                val json = assets.open("data.json").bufferedReader().use { it.readText() }
                repo.seedPlayersIfEmpty(json)
            }.onFailure { it.printStackTrace() }
        }
    }
}

