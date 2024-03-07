package com.ksalauyou.stockpricegraphic.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksalauyou.stockpricegraphic.data.ApiFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StockViewModel: ViewModel() {

    private val apiFactory = ApiFactory.apiService

    private val _state = MutableStateFlow<StockScreenState>(StockScreenState.Initial)
    val state = _state.asStateFlow()

    private var lastState : StockScreenState = StockScreenState.Initial

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("StockViewModel", "Exception: $throwable")
        _state.value = lastState
    }

    init {
        loadBarList()
    }

    fun loadBarList(timeFrame: TimeFrame = TimeFrame.HOUR_1) {
        lastState = _state.value
        _state.value = StockScreenState.Loading
        viewModelScope.launch(exceptionHandler) {
            val barList = apiFactory.loadBars(timeFrame.value).barList
            _state.value = StockScreenState.Content(barList, timeFrame)
        }
    }
}