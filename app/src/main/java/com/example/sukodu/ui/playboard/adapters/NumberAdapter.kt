package com.example.sukodu.ui.playboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import com.example.sukodu.R
import kotlinx.android.synthetic.main.number_view_holder.view.*

class NumberAdapter(private val callback : NumberCallback) : BaseAdapter() {

    private var numbers : List<Int>? = null

    var range : Int? = null
        set(value) {
            field = value
            numbers = value?.let { List(it) {number -> number + 1} }
        }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(parent?.context).inflate(
            R.layout.number_view_holder,
            parent,
            false)

        numbers?.get(position)?.let {
            view.number.text = it.toString()
            (view.number as Button).setOnClickListener { _ -> callback.onNumberClick(it) }
        }

        return view
    }

    override fun getItem(position: Int): Int? = numbers?.get(position)

    override fun getItemId(position: Int): Long = 0

    override fun getCount(): Int = numbers?.size ?: 0
}

interface NumberCallback {
    fun onNumberClick(number : Int)
}