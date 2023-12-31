package com.lwons.textureviewplayground

import android.graphics.Paint
import android.graphics.Paint.Style.FILL
import android.graphics.Paint.Style.STROKE
import android.graphics.Path

class StyledPath(val path: Path, stroke: StrokeStyle?, fill: Int?) {
    var strokePaint: Paint? = null
    var fillPaint: Paint? = null

    init {
        stroke?.let {
            strokePaint = Paint().also { paint ->
                paint.isAntiAlias = true
                paint.style = STROKE
                paint.color = it.color
                paint.strokeWidth = it.width
            }
        }

        fill?.let {
            fillPaint = Paint().also { paint ->
                paint.isAntiAlias = true
                paint.style = FILL
                paint.color = it
            }
        }

    }
}


data class StrokeStyle(val width: Float, val color: Int)