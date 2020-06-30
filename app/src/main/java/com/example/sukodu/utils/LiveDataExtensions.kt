package com.example.sukodu.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T> LiveData<T>.asMutable() : MutableLiveData<T> = this as MutableLiveData<T>
