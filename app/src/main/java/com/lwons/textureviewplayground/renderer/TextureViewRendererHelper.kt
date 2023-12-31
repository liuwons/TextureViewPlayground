package com.lwons.textureviewplayground.renderer

import android.util.Log
import android.view.TextureView
import com.lwons.textureviewplayground.duration
import com.lwons.textureviewplayground.renderer.IRenderer.IRenderCallback

class TextureViewRendererHelper(private val textureView: TextureView) {

    companion object {
        private const val TAG = "TextureViewRendererHelper"

        private const val FRAME_INTERVAL = 16L  // 60fps
    }

    private var ready = false
    @Volatile
    private var active = false
    private var renderThread: Thread? = null

    private var renderListener: IRenderCallback? = null

    fun setRenderCallback(listener: IRenderCallback) {
        renderListener = listener
    }

    fun onSurfaceTextureAvailable() {
        ready = true
        startRender()
    }

    fun onSurfaceTextureDestroyed() {
        ready = false
        stopRender()
    }

    private fun render() {
        while (active) {
            try {
                duration {
                    textureView.lockCanvas()?.let { canvas ->
                        renderListener?.onRender(canvas)
                        if (active) {
                            textureView.unlockCanvasAndPost(canvas)
                        }
                    }
                }.let {
                    if (it < FRAME_INTERVAL) {
                        Thread.sleep(FRAME_INTERVAL - it)
                    }
                }
            } catch (err: Throwable) {
                Log.e(TAG, "render failed: $err")
            }

        }
    }

    private fun startRender() {
        Log.i(TAG, "startRender")
        if (ready && renderThread == null) {
            active = true
            renderThread = Thread({ render() }, TAG).also { it.start() }
        } else {
            Log.e(TAG, "startRender failed: [ready]$ready")
        }
    }

    private fun stopRender() {
        active = false
        Log.i(TAG, "stopRender")
        renderThread?.let { thread ->
            while (true) {
                try {
                    thread.join(5000)
                    break
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        renderThread = null
    }


}