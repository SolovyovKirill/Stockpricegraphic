package com.ksalauyou.stockpricegraphic.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ksalauyou.stockpricegraphic.data.Bar
import kotlin.math.roundToInt

private const val MIN_VISIBLE_BARS_COUNT = 20

@Composable
fun Terminal(bars: List<Bar>) {

    var terminalState by rememberTerminalState(bars = bars)

    val transformableState = TransformableState { zoomChange, panChange, _ ->
        val visibleBarsCount = (terminalState.visibleBarsCount / zoomChange).roundToInt()
            .coerceIn(MIN_VISIBLE_BARS_COUNT, bars.size)
        val scrolledBy = (terminalState.scrolledBy + panChange.x)
            .coerceAtLeast(0f)
            .coerceAtMost(bars.size * terminalState.barWidth - terminalState.terminalWidth)

        terminalState = terminalState.copy(
            visibleBarsCount = visibleBarsCount,
            scrolledBy = scrolledBy
        )
    }

    val textMeasure = rememberTextMeasurer()
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(
                top = 32.dp,
                bottom = 32.dp
            )
            .transformable(transformableState)
            .onSizeChanged {
                terminalState = terminalState.copy(terminalWidth = it.width.toFloat())
            }
    ) {
        terminalState = terminalState.copy(terminalWidth = size.width)
        val max = terminalState.visibleBars.maxOf { it.high }
        val min = terminalState.visibleBars.minOf { it.low }

        val pxPerPoint = size.height / (max - min)
        translate(left = terminalState.scrolledBy) {
            bars.forEachIndexed { index, bar ->
                val offsetX = size.width - terminalState.barWidth * index
                drawLine(
                    color = Color.White,
                    start = Offset(offsetX, size.height - ((bar.low - min) * pxPerPoint)),
                    end = Offset(offsetX, size.height - ((bar.high - min) * pxPerPoint)),
                    strokeWidth = 1f
                )
                drawLine(
                    color = if (bar.open < bar.close) Color.Green else Color.Red,
                    start = Offset(offsetX, size.height - ((bar.open - min) * pxPerPoint)),
                    end = Offset(offsetX, size.height - ((bar.close - min) * pxPerPoint)),
                    strokeWidth = terminalState.barWidth / 2
                )
            }

        }
        bars.firstOrNull()?.let {
            drawPrices(
                max = max,
                min = min,
                pxPerPoint = pxPerPoint,
                lastPrice = it.close,
                textMeasurer = textMeasure
            )
        }

    }
}

private fun DrawScope.drawPrices(
    max: Float,
    min: Float,
    pxPerPoint: Float,
    lastPrice: Float,
    textMeasurer: TextMeasurer
) {
    val maxPriceOffsetY = 0f
    drawDashedLine(
        start = Offset(0f, maxPriceOffsetY),
        end = Offset(size.width, maxPriceOffsetY)
    )
    drawTextPrice(
        price = max,
        offsetY = maxPriceOffsetY,
        textMeasurer = textMeasurer
    )

    val lastPriceOffsetY = size.height - ((lastPrice - min) * pxPerPoint)
    drawDashedLine(
        start = Offset(0f, lastPriceOffsetY),
        end = Offset(size.width, lastPriceOffsetY)
    )
    drawTextPrice(
        price = lastPrice,
        offsetY = lastPriceOffsetY,
        textMeasurer = textMeasurer
    )

    val minPriceOffsetY = size.height
    drawDashedLine(
        start = Offset(0f, minPriceOffsetY),
        end = Offset(size.width, minPriceOffsetY)
    )
    drawTextPrice(
        price = min,
        offsetY = minPriceOffsetY,
        textMeasurer = textMeasurer
    )

}

private fun DrawScope.drawTextPrice(
    price: Float,
    offsetY: Float,
    textMeasurer: TextMeasurer
) {
    val textLayoutResult = textMeasurer.measure(
        text = price.toString(),
        style = TextStyle(
            color = Color.White,
            fontSize = 12.sp
        )
    )
    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(
            x = size.width - textLayoutResult.size.width,
            y = offsetY
        )
    )
}

private fun DrawScope.drawDashedLine(
    color: Color = Color.White,
    start: Offset,
    end: Offset,
    strokeWidth: Float = 1f
) {
    drawLine(
        color = color,
        start = start,
        end = end,
        strokeWidth = strokeWidth,
        pathEffect = PathEffect.dashPathEffect(
            floatArrayOf(4.dp.toPx(), 4.dp.toPx())
        )
    )
}