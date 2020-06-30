package com.example.sukodu.repository.sudokurepository

import com.example.sukodu.dto.BoardDto
import com.example.sukodu.repository.BaseApi
import com.example.sukodu.repository.RemoteRepositoryFactory
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

const val baseURL : String = "https://sugoku.herokuapp.com"

interface SudokuApi {

    @GET("/board")
    fun getBoard(@Query("difficulty") difficulty: String) : Single<BoardDto>

    companion object : BaseApi<SudokuApi> {
        override fun getBaseURL(): String = baseURL
        operator fun invoke(): SudokuApi = RemoteRepositoryFactory.createRepository(SudokuApi)
    }

}