package com.example.sukodu.model

import java.io.Serializable

data class Cell (
    var value : Int,
    val row : Int,
    val column : Int,
    val square : Int,
    val touchable : Boolean,
    var isValid : Boolean = true) : Serializable
