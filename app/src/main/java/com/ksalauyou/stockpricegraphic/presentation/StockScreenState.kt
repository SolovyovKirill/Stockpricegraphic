package com.ksalauyou.stockpricegraphic.presentation

import com.ksalauyou.stockpricegraphic.data.Bar

sealed class StockScreenState {
    object Initial : StockScreenState()
    data class Content(val bars: List<Bar>) : StockScreenState()
}