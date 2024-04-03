package co.geisyanne.volleymatch.di

import co.geisyanne.volleymatch.data.local.AppDatabase
import co.geisyanne.volleymatch.data.local.repositoryImpl.PlayerLocalDataSource
import co.geisyanne.volleymatch.domain.repository.PlayerRepository
import co.geisyanne.volleymatch.presentation.drawTeams.draw.DrawViewModel
import co.geisyanne.volleymatch.presentation.drawTeams.player.list.PlayerListViewModel
import co.geisyanne.volleymatch.presentation.drawTeams.player.register.RegisterPlayerViewModel
import co.geisyanne.volleymatch.presentation.drawTeams.result.ResultViewModel
import co.geisyanne.volleymatch.presentation.scoreboard.ScoreboardViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { DrawViewModel(get()) }

    viewModel { PlayerListViewModel(get()) }

    viewModel { RegisterPlayerViewModel(get()) }

    viewModel { ResultViewModel() }  // sem repo

    viewModel { ScoreboardViewModel() }  // sem repo

}

val repositoryModule = module {
    single<PlayerRepository> { PlayerLocalDataSource(get()) }
}

val daoModule = module {
    single { AppDatabase.getInstance(androidContext()).playerDao }
}

