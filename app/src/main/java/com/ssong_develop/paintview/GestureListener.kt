package com.ssong_develop.paintview

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent

class GestureListener(
    private val callback : (Boolean) -> Unit
) : GestureDetector.SimpleOnGestureListener() {
    private val TAG = "GestureListener"

    private var isScale = false

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        Log.e(TAG,"onDoubleTap")
        isScale = !isScale
        callback.invoke(isScale)
        return true
    }
}