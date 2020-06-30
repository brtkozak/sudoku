package com.example.sukodu.utils

import com.example.sukodu.model.Cell

class BoardValidator {

    /**
   * Board validation:
   * Divide [cells] representing whole board into lists containing list of all squares/rows/columns
   * and run [validateGroups] on each list
   */
    fun validateBoard(cells : List<Cell>?) : Boolean {
        if (cells == null)
            return false

        val areSquaresValid = validateGroups(cells.groupBy { it.square }.values.toList())
        if(!areSquaresValid) return false

        val areRowsValid = validateGroups(cells.groupBy { it.row }.values.toList())
        if(!areRowsValid) return false

        val areColumnsValid = validateGroups(cells.groupBy { it.column }.values.toList())
        if(!areColumnsValid) return false

        return true
    }

    /**
     * Groups validation:
     * [group] is list of list containing all squares/rows/columns
     * Check if there is no repeat of value in each of the square/row/column
     */
    private fun validateGroups(group : List<List<Cell>>) : Boolean{
        group.map { it.filter { cell -> cell.value != 0 } }.forEach { square ->
            if(square.size != square.distinctBy { it.value }.size)
                return false
        }
        return true
    }


    /*
     * Single cell validation:
     * Check if the value of cell to be validate is unique in its square/column/row
     */
    fun validateCell(cells : List<Cell>?, toValidate : Cell?) : Boolean{
        if(cells == null || toValidate == null)
            return false

        val areSquaresValid = cells.filter { it.square == toValidate.square && it.value == toValidate.value }.size == 1
        if (!areSquaresValid) return false

        val areRowsValid = cells.filter { it.row == toValidate.row && it.value == toValidate.value }.size == 1
        if (!areRowsValid) return false

        val areColumnsValid = cells.filter { it.column == toValidate.column && it.value == toValidate.value }.size == 1
        if (!areColumnsValid) return false

        return true
    }

}