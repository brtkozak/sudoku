package com.example.sukodu.ui.playboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.example.sukodu.R
import com.example.sukodu.model.Cell
import kotlinx.android.synthetic.main.cell_view_holder.view.*

class CellAdapter(private val callback : CellCallback, private val squareAdapter: SquareAdapter) : BaseAdapter() {

    var cells : List<Cell>? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.cell_view_holder, parent, false)
        val currentSelected = squareAdapter.currentSelectedCell

        cells?.get(position)?.let { cell ->
            view.cell_value.text = cell.value.let { if(it == 0) "" else it.toString()}

            if (currentSelected != null) {
                if (cell == currentSelected) {
                    view.setCellStyle(CellState.SELECTED)
                } else if (cell.square == currentSelected.square || cell.row == currentSelected.row || cell.column == currentSelected.column) {
                    view.setCellStyle(CellState.CONNECTED)
                } else {
                    view.setCellStyle(CellState.NONE)
                }
            } else {
                view.setCellStyle(CellState.NONE)
            }
            view.setError(!cell.isValid)
            if (cell.touchable) view.setOnClickListener { callback.onCellClick(cell) }
        }

        return view
    }

    override fun getItem(position: Int): Cell? = cells?.get(position)

    override fun getItemId(position: Int): Long = 0

    override fun getCount(): Int = cells?.size ?: 0

    private fun View.setCellStyle(cellState: CellState) {
        val color = when (cellState) {
            CellState.SELECTED -> R.color.colorSecondaryDark
            CellState.CONNECTED -> R.color.colorSecondary
            CellState.NONE -> null
        }
        if (color != null)
            cell_value.setBackgroundColor(ContextCompat.getColor(context, color))
        else cell_value.background = null
    }

    private fun View.setError(error: Boolean){
        if (error)
            cell_value.setTextColor(ContextCompat.getColor(context, R.color.error))
        else
            cell_value.setTextColor(ContextCompat.getColor(context, R.color.cellText))

    }

    enum class CellState {
        SELECTED,
        CONNECTED,
        NONE
    }

}

interface CellCallback {
    fun onCellClick(cell: Cell)
}

