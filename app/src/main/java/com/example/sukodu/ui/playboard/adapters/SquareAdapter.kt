package com.example.sukodu.ui.playboard.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.sukodu.R
import com.example.sukodu.model.Cell
import kotlinx.android.synthetic.main.square_view_holder.view.*
import java.lang.StrictMath.sqrt
import java.util.*

class SquareAdapter(private val callBack : CellCallback) : BaseAdapter() {

    /**
     * [cellAdapters] state of list of [CellAdapter]
     * Holds adapter for each of squares
     */
    private var cellAdapters: MutableList<CellAdapter>? = null

    /**
     * [currentSelectedCell] state of [Cell]
     * Current selected cell by the user
     * Used to draw correct style of the cells
     */
    var currentSelectedCell : Cell? = null
        set(value) {
            previousSelectedCell = currentSelectedCell
            field = value
            setToNotifyMap()
            notifyDataSetChanged()
        }

    /**
     * [previousSelectedCell] state of [Cell]
     * Previous selected cell by the user
     * Used to minimize calling notifyDataSetChange on [cellAdapters]
     */
    private var previousSelectedCell : Cell? = null

    /**
     * [toNotify] state of [Map]
     * Holds indexes of rows, columns and squares to be notified about data set change
     * Used to minimize calling notifyDataSetChange on [cellAdapters]
     */
    private val toNotify : MutableMap<ToNotifyKey, List<Int>> = EnumMap(ToNotifyKey::class.java)

    var squares: List<List<Cell>>? = null
        set(value) {
            field = value
            if (cellAdapters == null)
                cellAdapters = MutableList(squares?.size ?: 0) { CellAdapter(callBack, this) }
        }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(parent?.context).inflate(
            R.layout.square_view_holder,
            parent,
            false)
        val square = squares?.get(position)

        var notify = true
        if(currentSelectedCell != null && square != null){
            notify = shouldNotifySquare(square)
        }

        if(notify) {
            square?.let {
                view.square.numColumns = sqrt(square.size.toDouble()).toInt()
                cellAdapters?.get(position)?.let { cellAdapter ->
                    if (view.square.adapter == null) view.square.adapter = cellAdapter
                    cellAdapter.cells = square
                    cellAdapter.notifyDataSetChanged()
                }
            }
        }
        return view
    }

    override fun getItem(position: Int): List<Cell>? = squares?.get(position)

    override fun getItemId(position: Int): Long = 0

    override fun getCount(): Int = squares?.size ?: 0

    private fun setToNotifyMap(){
        toNotify[ToNotifyKey.COLUMN] = listOfNotNull(currentSelectedCell?.column, previousSelectedCell?.column).distinct()
        toNotify[ToNotifyKey.ROW] = listOfNotNull(currentSelectedCell?.row, previousSelectedCell?.row).distinct()
        toNotify[ToNotifyKey.SQUARE] = listOfNotNull(currentSelectedCell?.square, previousSelectedCell?.square).distinct()
    }

    private fun shouldNotifySquare(square : List<Cell>) : Boolean{
        toNotify[ToNotifyKey.SQUARE]?.let {
            if(it.contains(square[0].square))
                return true
        }
        toNotify[ToNotifyKey.COLUMN]?.let {columnsToNotify ->
            if(columnsToNotify.intersect(square.map { s -> s.column }).isNotEmpty())
                return true
        }
        toNotify[ToNotifyKey.ROW]?.let {rowsToNotify ->
            if(rowsToNotify.intersect(square.map { s -> s.row }).isNotEmpty())
                return true
        }
        return false
    }
}

enum class ToNotifyKey {
    COLUMN, ROW, SQUARE
}
