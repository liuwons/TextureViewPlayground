package com.lwons.textureviewplayground

internal inline fun duration(block: () -> Unit): Long {
    val tm = System.currentTimeMillis()
    block()
    return System.currentTimeMillis() - tm
}