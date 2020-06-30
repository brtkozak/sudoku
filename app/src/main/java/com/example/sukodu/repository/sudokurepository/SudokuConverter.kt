package com.example.sukodu.repository.sudokurepository

import com.example.sukodu.dto.BoardDto
import com.example.sukodu.model.Cell
import kotlin.math.sqrt

object SudokuConverter {

    fun convertBoardToCells(dto : BoardDto) : List<Cell>? {
        val cells = mutableListOf<Cell>()
        for(row in dto.board.indices){
            for(column in dto.board[row].indices){
                val square = getSquare(row, column, dto.board.size )
                if(square == -1) return null
                cells.add(Cell(dto.board[row][column], row, column, square, dto.board[row][column] == 0))
            }
        }
        return cells
    }

    /**
     * Calculate square based on coordinates
     * For 9x9 sudoku there are 9 3x3 squares
     */
    private fun getSquare(x : Int, y : Int, boardSize : Int) : Int {
        val step = sqrt(boardSize.toDouble()).toInt()
        var square = 1
        for(row in 0 until boardSize step step){
            for(column in 0 until boardSize step step){
                if(x in row until row + step && y in column until column + step)
                    return square
                square ++
            }
        }
        return -1
    }

}