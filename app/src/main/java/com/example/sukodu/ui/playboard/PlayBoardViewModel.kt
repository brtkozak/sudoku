package com.example.sukodu.ui.playboard

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sukodu.R
import com.example.sukodu.model.Cell
import com.example.sukodu.utils.BoardValidator
import com.example.sukodu.utils.asMutable
import org.koin.core.KoinComponent
import org.koin.core.inject

class PlayBoardViewModel : ViewModel(), KoinComponent {

    /**
     * [cells] state of list of [Cell]
     * Holds all cells based on which the board is generated
     */
    val cells : LiveData<List<Cell>> = MutableLiveData<List<Cell>>()

    /**
     * [currentSelectedCell] state of list of [Cell]
     * Holds reference to current selected cell by user
     * Always should point to one of the [cells]
     */
    val currentSelectedCell : LiveData<Cell> = MutableLiveData<Cell>()

    /**
     * [message] state of [Int]
     * Should always be [StringRes] and used to show toast on UI
     */
    val message : LiveData<Int> = MutableLiveData<Int>()

    /**
     * [boardSolved] state of [Boolean]
     * The value is true when the user correctly solve board and press the check button
     */
    val boardSolved : LiveData<Boolean> = MutableLiveData<Boolean>()

    /**
     * [liveValidationEnabled] state of [Boolean]
     * Specifies whether the live validation option is enabled
     */
    var liveValidationEnabled = false
        set(value) {
            field = value
            forceUiRefresh()
            validateBoard()
        }

    /**
     * [boardValidator] state of [BoardValidator]
     * Validator class used to check if single cell is valid when live validation is enabled
     * or to validate whole board when the user press the check button
     */
    private val boardValidator : BoardValidator by inject()

    fun setCell(cells : List<Cell>){
        this.cells.asMutable().value = cells
    }

    fun setCurrentSelectedCell(cell : Cell) {
        this.currentSelectedCell.asMutable().value = cell
    }

    fun updateCurrentSelectedValue(value: Int) {
        cells.value?.firstOrNull { it == currentSelectedCell.value }?.value = value

        var cellValid = true
        if (liveValidationEnabled) {
            cellValid = boardValidator.validateCell(cells.value, currentSelectedCell.value)
            validateBoard()
        }
        currentSelectedCell.value?.let {
            setCurrentSelectedCell(it.apply {
                this.value = value
                this.isValid = cellValid
            })
        }
    }

    private fun validateBoard(){
        currentSelectedCell.value?.let {
            cells.value?.filter { cell -> !cell.isValid && cell != it }?.forEach { cell ->
                cell.apply { this.isValid = boardValidator.validateCell(cells.value, cell) }
            }
        }
    }

    fun clearCurrentSelectedValue() {
        currentSelectedCell.value?.let {
            setCurrentSelectedCell(it.apply {
                value = 0
                isValid = true})
        }
        if(liveValidationEnabled)
            validateBoard()
    }

    fun checkResult(){
        val boardFullyFilled = cells.value?.firstOrNull { it.value == 0 } == null
        if (boardFullyFilled) {
            if (boardValidator.validateBoard(cells.value))
                boardSolved.asMutable().value = true
            else
                message.asMutable().value = R.string.incorrect_solution
        }
        else
            message.asMutable().value = R.string.fill_all_cells
    }

    fun resetBoard() {
        cells.value?.filter { it -> it.touchable }?.forEach { cell ->
            cell.apply {
                value = 0
                isValid = true}
        }
        forceUiRefresh()
    }

    private fun forceUiRefresh(){
        currentSelectedCell.value?.let {
            updateCurrentSelectedValue(it.value)
        }
    }

}
