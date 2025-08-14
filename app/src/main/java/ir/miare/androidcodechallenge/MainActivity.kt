package ir.miare.androidcodechallenge

import PlayersViewModelFactory
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import ir.miare.androidcodechallenge.local.AppDatabase
import ir.miare.androidcodechallenge.remote.PlayerApi
import ir.miare.androidcodechallenge.repository.PlayerRepositoryImpl
import ir.miare.androidcodechallenge.ui.followed.FollowedPlayersScreen
import ir.miare.androidcodechallenge.ui.players.PlayersScreen
import ir.miare.androidcodechallenge.usecase.FollowPlayerUseCase
import ir.miare.androidcodechallenge.usecase.GetFollowedPlayersUseCase
import ir.miare.androidcodechallenge.usecase.GetPlayersPagerUseCase
import ir.miare.androidcodechallenge.viewmodel.PlayersViewModel
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: PlayersViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        val repo = PlayerRepositoryImpl(api, db)

        val getPlayersPagerUseCase = GetPlayersPagerUseCase(repo)
        val followPlayerUseCase = FollowPlayerUseCase(repo)
        val getFollowedPlayersUseCase = GetFollowedPlayersUseCase(repo)

        val viewModel = ViewModelProvider(
            this,
            PlayersViewModelFactory(
                getPlayersPagerUseCase,
                followPlayerUseCase,
                getFollowedPlayersUseCase
            )
        )[PlayersViewModel::class.java]


        setContent {
            var showFollowed by remember { mutableStateOf(false) }

            if (showFollowed) {
                FollowedPlayersScreen(viewModel = viewModel, onBack = { showFollowed = false })
            } else {
                PlayersScreen(
                    viewModel = viewModel,
                    onOpenFollowed = { showFollowed = true }
                )
            }
        }
    }
}
