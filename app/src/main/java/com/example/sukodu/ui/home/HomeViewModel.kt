package com.example.sukodu.ui.home

import android.util.Log
import android.widget.BaseAdapter
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sukodu.R
import com.example.sukodu.model.Cell
import com.example.sukodu.repository.Difficulty
import com.example.sukodu.repository.RemoteRepository
import com.example.sukodu.utils.asMutable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.home_fragment.view.*

class HomeViewModel(private val remoteRepository: RemoteRepository) : ViewModel() {

    private var disposables: CompositeDisposable = CompositeDisposable()

    /**
     * [inProgress] state of [Boolean]
     * used to show progress bar on UI while waiting for server response
     */
    val inProgress : LiveData<Boolean> = MutableLiveData<Boolean>()

    /**
     * [cells] state of list of [Cell]
     * used to open play board with data after receiving success response from server
     */
    val cells : LiveData<List<Cell>> = MutableLiveData<List<Cell>>()

    /**
     * [message] state of [Int]
     * Should always be [StringRes] and used to show toast on UI
     */
    val message : LiveData<Int> = MutableLiveData<Int>()

    fun onLevelSelect(difficulty: Difficulty){
        disposables.add(remoteRepository.getBoard(difficulty.requestParam)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { inProgress.asMutable().value = true }
            .doAfterTerminate { inProgress.asMutable().value = false }
            .subscribeBy(onSuccess = {
                cells.asMutable().value = (it)
            }, onError = {
                Log.d("HomeViewModel", "On level select error: ${it.message}" )
                message.asMutable().postValue(R.string.cannot_load_play_board)
            }))
    }

    fun setCells(cells : List<Cell>?) {
        this.cells.asMutable().postValue(cells)
    }

    public override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

}
