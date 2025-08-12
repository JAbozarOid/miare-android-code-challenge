package ir.miare.androidcodechallenge

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ir.miare.androidcodechallenge.repository.PlayerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application() {

    @Inject
    lateinit var repo: PlayerRepository

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val json = assets.open("data.json").bufferedReader().use { it.readText() }
                repo.seedPlayersIfEmpty(json)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
