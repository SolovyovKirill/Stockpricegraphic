package com.ksalauyou.stockpricegraphic.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.ksalauyou.stockpricegraphic.data.Bar

@Composable
fun Terminal(bars: List<Bar>) {
    Canvas(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
    ) {
        bars.forEachIndexed { index, bar ->
            val max = bars.maxOf { it.high }
            val min = bars.minOf { it.low }
            val barWidth = size.width / bars.size
            val offsetX = barWidth * index
            val pxPerPoint = size.height / (max - min)
            drawLine(
                color = Color.White,
                start = Offset(offsetX, size.height - ((bar.low - min) * pxPerPoint)),
                end = Offset(offsetX    , size.height - ((bar.high - min) * pxPerPoint)),
                strokeWidth = barWidth
            )
        }

    }

}