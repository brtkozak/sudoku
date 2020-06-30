package com.example.sukodu.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.sukodu.R
import com.example.sukodu.databinding.HomeFragmentBinding
import com.example.sukodu.model.Cell
import com.example.sukodu.ui.playboard.PlayBoardFragment
import com.example.sukodu.utils.asMutable
import kotlinx.android.synthetic.main.home_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModel()
    private lateinit var binding : HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container,false)
        binding.let {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        viewModel.cells.observe(viewLifecycleOwner, Observer {
            if(it != null)
                openPlayBoard(it)
            viewModel.setCells(null)
        })

        viewModel.message.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, getString(it), Toast.LENGTH_SHORT).show()
        })
    }

    private fun openPlayBoard(cells : List<Cell>){
        val bundle = bundleOf(PlayBoardFragment.EXTRAS_KEY to cells.toTypedArray())
        view?.findNavController()?.navigate(R.id.action_homeFragment_to_playBoardFragment, bundle)
    }

}
