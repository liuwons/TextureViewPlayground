package com.lwons.textureviewplayground

import android.content.pm.ActivityInfo
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.lwons.textureviewplayground.renderer.IRenderer.IRenderCallback
import com.lwons.textureviewplayground.renderer.impl.HwTextureViewRenderer
import com.lwons.textureviewplayground.renderer.impl.TextureViewRenderer

class MainActivity : AppCompatActivity(), IRenderCallback {
    companion object {
        private const val TAG = "MainActivity"
    }

    private val pathList = arrayListOf<StyledPath>()
    private var curFrame = 0

    private var currentRendererHw: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        setContentView(R.layout.activity_main)

        loadRenderPathList()

        onRendererChanged(currentRendererHw)

        findViewById<View>(R.id.btn_texture_view_renderer).setOnClickListener {
            if (currentRendererHw) {
                onRendererChanged(false)
            }
        }

        findViewById<View>(R.id.btn_hw_texture_view_renderer).setOnClickListener {
            if (!currentRendererHw) {
                onRendererChanged(true)
            }
        }

    }

    override fun onRender(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)

        duration {
            pathList.forEach { path ->
                path.strokePaint?.let { paint ->
                    canvas.drawPath(path.path, paint)
                }
                path.fillPaint?.let { paint ->
                    canvas.drawPath(path.path, paint)
                }
            }
        }.let {
            Log.i(TAG, "render time: $it")
        }

        curFrame += 1
    }

    private fun onRendererChanged(hw: Boolean) {
        currentRendererHw = hw
        findViewById<TextView>(R.id.btn_hw_texture_view_renderer).setTextColor(if (hw) Color.GREEN else Color.BLACK)
        findViewById<TextView>(R.id.btn_texture_view_renderer).setTextColor(if (hw) Color.BLACK else Color.GREEN)

        val renderer = if (hw) HwTextureViewRenderer(this) else TextureViewRenderer(this)
        renderer.setRenderCallback(this)
        findViewById<FrameLayout>(R.id.fl_renderer_container).let { container ->
            container.removeAllViews()
            container.addView(renderer, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
        }
    }

    private fun loadRenderPathList() {
        pathList.clear()
        pathList.addAll(PathUtils.parse(applicationContext.assets.open("path/path30.json").bufferedReader().use { it.readText() }))
        Log.i(TAG, "loadRenderPathList load ${pathList.size} path")
    }

}