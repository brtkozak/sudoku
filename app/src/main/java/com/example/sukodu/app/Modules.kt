package com.example.sukodu.app

import com.example.sukodu.repository.RemoteRepository
import com.example.sukodu.ui.home.HomeViewModel
import com.example.sukodu.ui.playboard.PlayBoardViewModel
import com.example.sukodu.utils.BoardValidator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Modules {

    val modules by lazy {
        listOf(
            commonModule,
            viewModelsModule,
            repositoriesModule
        )
    }

    private val commonModule = module {
        factory { BoardValidator() }
    }

    private val viewModelsModule = module {
        viewModel { HomeViewModel(get()) }
        viewModel { PlayBoardViewModel() }
    }

    private val repositoriesModule = module {
        single { RemoteRepository() }
    }

}