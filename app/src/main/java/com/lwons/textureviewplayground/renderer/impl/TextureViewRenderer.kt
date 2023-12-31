package com.lwons.textureviewplayground.renderer.impl

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.util.Log
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import com.lwons.textureviewplayground.renderer.TextureViewRendererHelper
import com.lwons.textureviewplayground.renderer.IRenderer
import com.lwons.textureviewplayground.renderer.IRenderer.IRenderCallback

class TextureViewRenderer @JvmOverloads constructor(ctx: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    TextureView(ctx, attrs, defStyle), SurfaceTextureListener, IRenderer {

    private val helper = TextureViewRendererHelper(this)

    companion object {
        private const val TAG = "TextureViewRenderer"
    }

    init {
        surfaceTextureListener = this
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        Log.i(TAG, "onSurfaceTextureAvailable: [w]$width [h]$height")
        helper.onSurfaceTextureAvailable()
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        Log.i(TAG, "onSurfaceTextureDestroyed")
        helper.onSurfaceTextureDestroyed()
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
    }

    override fun setRenderCallback(callback: IRenderCallback) {
        helper.setRenderCallback(callback)
    }


}