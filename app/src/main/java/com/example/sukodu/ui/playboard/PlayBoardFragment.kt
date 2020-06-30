package com.example.sukodu.ui.playboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController

import com.example.sukodu.R
import com.example.sukodu.databinding.PlayBoardFragmentBinding
import com.example.sukodu.model.Cell
import com.example.sukodu.ui.home.HomeFragment
import com.example.sukodu.ui.playboard.adapters.*
import kotlinx.android.synthetic.main.play_board_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.sqrt

class PlayBoardFragment : Fragment(), CellCallback, NumberCallback{

    private val viewModel: PlayBoardViewModel by viewModel()
    private lateinit var binding : PlayBoardFragmentBinding

    private val squareAdapter = SquareAdapter(this)
    private val numberAdapter = NumberAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.play_board_fragment, container,false)
        binding.let {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initObservers()
        initListeners()
        arguments?.let {
            val cells = it.get(EXTRAS_KEY) as Array<Cell>
            prepareGridViews(cells.size)
            viewModel.setCell(cells.toList())
        }
    }

    private fun initObservers(){
        viewModel.cells.observe(viewLifecycleOwner, Observer {
            squareAdapter.squares = it.groupBy { cell -> cell.square }.values.toList()
            squareAdapter.notifyDataSetChanged()
        })

        viewModel.currentSelectedCell.observe(viewLifecycleOwner, Observer {
            squareAdapter.currentSelectedCell = it
        })

        viewModel.message.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.boardSolved.observe(viewLifecycleOwner, Observer {
            if(it) Toast.makeText(context, R.string.win, Toast.LENGTH_LONG).show()
        })
    }

    private fun initListeners(){
        live_validation.setOnCheckedChangeListener { _, isChecked ->
            viewModel.liveValidationEnabled = isChecked
        }
    }

    private fun prepareGridViews(elementsCount : Int){
        val squareViewColumns = sqrt(sqrt(elementsCount.toDouble())).toInt()
        board.numColumns = squareViewColumns
        board.adapter = squareAdapter

        val numberViewColumns = squareViewColumns * squareViewColumns
        numbers.numColumns = numberViewColumns
        numbers.adapter = numberAdapter
        numberAdapter.range = numberViewColumns
        numberAdapter.notifyDataSetChanged()
    }

    override fun onCellClick(cell: Cell) {
        viewModel.setCurrentSelectedCell(cell)
    }

    override fun onNumberClick(number: Int) {
        viewModel.currentSelectedCell.value?.let {
            viewModel.updateCurrentSelectedValue(number)
        }
    }

    companion object {
        const val EXTRAS_KEY = "cells"
    }
}

