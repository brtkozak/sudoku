package com.example.sukodu

import com.example.sukodu.dto.BoardDto
import com.example.sukodu.model.Cell
import com.example.sukodu.repository.sudokurepository.SudokuConverter
import com.example.sukodu.utils.BoardValidator
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testSquareCalculation(){
        val dto = BoardDto(mutableListOf(listOf(1,2,3), listOf(1,2,3), listOf(1,2,3)))
        val cells = SudokuConverter.convertBoardToCells(dto) as MutableList<Cell>
        assert(cells[0].square == 1)
        assert(cells[8].square == 9)
    }

    @Test
    fun testSingleCellValidation(){
        val cells = listOf(
            Cell(1,1,1,1, false),
            Cell(2,2,2,2,false),
            Cell(2,2,3,3,false))

        val validator = BoardValidator()

        var toValide = cells[0]
        var isValid = validator.validateCell(cells, toValide)
        assert(isValid)

        toValide = cells[2]
        isValid = validator.validateCell(cells, toValide)
        assert(!isValid)
    }

    @Test
    fun testValidateBoard(){
       var cells = mutableListOf(
           Cell(1,1,1,1,false),
           Cell(1, 1,2,3,false))

        val validator = BoardValidator()

        var isValid = validator.validateBoard(cells)
        assert(!isValid)

        cells = mutableListOf(
            Cell(1,1,1,1,false),
            Cell(2, 1,2,1,false))

        isValid = validator.validateBoard(cells)
        assert(isValid)

        cells.clear()
        for(i in 1..81) {
            cells.add(Cell(i, i, i, i, false))
        }

        isValid = validator.validateBoard(cells)
        assert(isValid)

        cells.clear()
        for(i in 1..81) {
            cells.add(Cell(i%9, i%9, i%9, i%9, false))
        }

        isValid = validator.validateBoard(cells)
        assert(!isValid)
    }

}
