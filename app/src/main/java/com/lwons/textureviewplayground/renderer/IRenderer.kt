package com.lwons.textureviewplayground.renderer

import android.graphics.Canvas

interface IRenderer {

    fun setRenderCallback(callback: IRenderCallback)

    interface IRenderCallback {
        fun onRender(canvas: Canvas)
    }
}