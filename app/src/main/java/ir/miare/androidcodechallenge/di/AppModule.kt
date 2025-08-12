package ir.miare.androidcodechallenge.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.miare.androidcodechallenge.repository.PlayerRepository
import ir.miare.androidcodechallenge.usecase.FollowPlayerUseCase
import ir.miare.androidcodechallenge.usecase.GetFollowedPlayersUseCase
import ir.miare.androidcodechallenge.usecase.GetPlayersPagerUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideGetPlayersPagerUseCase(repo: PlayerRepository) = GetPlayersPagerUseCase(repo)

    @Provides
    @Singleton
    fun provideFollowPlayerUseCase(repo: PlayerRepository) = FollowPlayerUseCase(repo)

    @Provides
    @Singleton
    fun provideGetFollowedPlayersUseCase(repo: PlayerRepository) = GetFollowedPlayersUseCase(repo)
}