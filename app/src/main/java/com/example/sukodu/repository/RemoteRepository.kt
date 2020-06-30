package com.example.sukodu.repository

import com.example.sukodu.model.Cell
import com.example.sukodu.repository.sudokurepository.SudokuApi
import com.example.sukodu.repository.sudokurepository.SudokuConverter
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent

class RemoteRepository : KoinComponent{

    private val api : SudokuApi = SudokuApi()

    fun getBoard(difficulty : String) : Single<List<Cell>> {
        return api.getBoard(difficulty)
            .subscribeOn(Schedulers.io())
            .map { SudokuConverter.convertBoardToCells(it) }
    }
}

enum class Difficulty(val requestParam : String){
    EASY("easy"),
    MEDIUM("medium"),
    HARD("hard")
}
