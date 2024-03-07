package com.ksalauyou.stockpricegraphic.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.ksalauyou.stockpricegraphic.data.Bar
import kotlin.math.roundToInt

private const val MIN_VISIBLE_BARS_COUNT = 20

@Composable
fun Terminal(bars: List<Bar>) {
    var visibleBarsCount by remember {
        mutableStateOf(100)
    }

    val transformableState = TransformableState { zoomChange, _, _ ->
        visibleBarsCount = (visibleBarsCount / zoomChange).roundToInt()
            .coerceIn(MIN_VISIBLE_BARS_COUNT, bars.size)
    }
    Canvas(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
        .transformable(transformableState)
    ) {
        bars.take(visibleBarsCount).forEachIndexed { index, bar ->
            val max = bars.maxOf { it.high }
            val min = bars.minOf { it.low }
            val barWidth = size.width / visibleBarsCount
            val offsetX = size.width - barWidth * index
            val pxPerPoint = size.height / (max - min)
            drawLine(
                color = Color.White,
                start = Offset(offsetX, size.height - ((bar.low - min) * pxPerPoint)),
                end = Offset(offsetX    , size.height - ((bar.high - min) * pxPerPoint)),
                strokeWidth = 1f
            )
        }

    }

}