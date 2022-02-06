package com.ssong_develop.paintview

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView

class GestureImageView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatImageView(context, attributeSet, defStyle) {

    private val gestureListener = GestureListener(callback = {
        if (it) {
            this.scaleX = 1.2f
            this.scaleY = 1.2f
        } else {
            this.scaleX = 1.0f
            this.scaleY = 1.0f
        }
    })

    private val zoomListener = ZoomListener(callback = { scaleSize ->
        this.scaleX = scaleSize
        this.scaleY = scaleSize
    })

    private val scaleGestureDetector: ScaleGestureDetector =
        ScaleGestureDetector(context, zoomListener)

    private val gestureDetector = GestureDetector(context, gestureListener)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(event.pointerCount == 2){
            scaleGestureDetector.onTouchEvent(event)
        } else {
            gestureDetector.onTouchEvent(event)
        }
        invalidate()
        return true
    }
}