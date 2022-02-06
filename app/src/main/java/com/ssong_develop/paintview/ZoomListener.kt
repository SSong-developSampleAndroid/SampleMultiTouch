package com.ssong_develop.paintview

import android.util.Log
import android.view.ScaleGestureDetector

class ZoomListener(
    private val callback : (Float) -> Unit
) : ScaleGestureDetector.SimpleOnScaleGestureListener() {

    private val TAG = "ScaleGestureDetector"
    private var scaleFactor = 1.0f

    private var isScale = false

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        Log.e(TAG,"onScale Invoke")
        scaleFactor *= detector.scaleFactor

        scaleFactor = Math.max(0.1f,Math.min(scaleFactor,10.0f))
        isScale = !isScale
        callback.invoke(scaleFactor)
        return true
    }
}