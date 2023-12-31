package com.lwons.textureviewplayground

import android.graphics.Path
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

object PathUtils {
    private const val TAG = "PathUtils"

    private const val KEY_ACTIONS = "actions"
    private const val KEY_POINTS = "points"
    private const val KEY_STROKE = "stroke"
    private const val KEY_COLOR = "color"
    private const val KEY_WIDTH = "width"
    private const val KEY_FILL = "fill"

    private const val ACTION_MOVE_TO = 1
    private const val ACTION_LINE_TO = 2
    private const val ACTION_QUAD_TO = 3
    private const val ACTION_CUBIC_TO = 4
    private const val ACTION_CLOSE = 5
    private const val ACTION_END = 6

    fun parse(json: String): List<StyledPath> {
        val lst = arrayListOf<StyledPath>()
        try {
            lst.addAll(parse(JSONArray(json)))
        } catch (err: Throwable) {
            Log.e(TAG, "parse path list failed: [err]$err")
        }
        return lst
    }

    private fun parse(array: JSONArray): Array<StyledPath> {
        return Array(array.length()) { parse(array.getJSONObject(it)) }
    }

    private fun parse(json: JSONObject): StyledPath {
        val arrAction = json.getJSONArray(KEY_ACTIONS)
        val actions = Array(arrAction.length()) { arrAction.getInt(it) }
        val arrPoints = json.getJSONArray(KEY_POINTS)
        val points = Array(arrPoints.length()) { arrPoints.getDouble(it).toFloat() }

        val path = Path()
        var idx = 0
        actions.forEach { action ->
            when (action) {
                ACTION_MOVE_TO -> {
                    path.moveTo(points[idx++], points[idx++])
                }
                ACTION_LINE_TO -> {
                    path.lineTo(points[idx++], points[idx++])
                }
                ACTION_QUAD_TO -> {
                    path.quadTo(points[idx++], points[idx++], points[idx++], points[idx++])
                }
                ACTION_CUBIC_TO -> {
                    path.cubicTo(points[idx++], points[idx++], points[idx++], points[idx++], points[idx++], points[idx++])
                }
                ACTION_CLOSE -> {
                    path.close()
                }
                ACTION_END -> {}
            }
        }

        var stroke: StrokeStyle? = null
        if (json.has(KEY_STROKE)) {
            json.getJSONObject(KEY_STROKE).let {
                stroke = StrokeStyle(it.getDouble(KEY_WIDTH).toFloat(), it.getInt(KEY_COLOR))
            }
        }

        var fill: Int? = null
        if (json.has(KEY_FILL)) {
            fill = json.getInt(KEY_FILL)
        }

        return StyledPath(path, stroke, fill)
    }

}