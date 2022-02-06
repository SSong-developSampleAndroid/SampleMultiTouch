package com.ssong_develop.paintview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


/**
 *
 * 멀티 터치 샘플
 *
 * 멀티터치 시 개별 손가락을 추적하는 것이 중요하다.
 *
 * MotionEvent 이벤트느 포인터와 관련된 모든 상호작용을 할 수 있는 핵심 클래스여서 이를 잘 사용하면 할 수 있다.
 *
 *
 */
class PaintView @JvmOverloads constructor(
    context : Context,
    attributeSet : AttributeSet? = null,
    defStyle : Int = 0
) : View(context) {
    // constants

    // 최대 손가락 갯수 5개
    private val MAX_FINGERS = 5

    // 각 손가락이 그릴 Path Value
    private val fingerPaths = Array<Path?>(5){_ -> null}

    // Path에 색을 입혀줄 Paint 객체
    private val fingerPaint = Paint()

    // touch Event가 down에서 up이 된 순간을 그렸다고 표현생각하고 ACTION_UP이 발생한 순간 그려진 Path를 담아줄 list
    private val completedPaths = ArrayList<Path>()

    private val pathBounds = RectF()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setPaint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for(completedPath in completedPaths){
            canvas.drawPath(completedPath, fingerPaint)
        }

        for(fingerPath in fingerPaths){
            if (fingerPath != null) {
                canvas.drawPath(fingerPath,fingerPaint)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val pointerCount = event.pointerCount // 현재 몇개만큼 multiTouch가 일어났는지 알려주는 변수
        val cappedPointerCount = if(pointerCount > MAX_FINGERS) MAX_FINGERS else pointerCount // 최대 5개만 인정해줌
        val actionIndex = event.actionIndex // action이 일어났던 순서를 가진 value
        val action = event.actionMasked // action이름 up,down,move 이런 류의 action을 의미한다.
        val id = event.getPointerId(actionIndex)

        if((action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) && id < MAX_FINGERS){
            // 그림을 그리기위해 손가락을 눌렀을 떄
            fingerPaths[id] = Path()  // Path객체를 생성
            fingerPaths[id]?.moveTo(event.getX(actionIndex),event.getY(actionIndex)) // Path를 action이 잡힌 x,y 좌표로 이동
        } else if((action == MotionEvent.ACTION_POINTER_UP || action == MotionEvent.ACTION_UP) && id < MAX_FINGERS){
            // 그림을 다 그렸을 때
            fingerPaths[id]?.setLastPoint(event.getX(actionIndex),event.getY(actionIndex)) // Path에 마지막 좌표 저장
            fingerPaths[id]?.let { completedPaths.add(it) } // 그림을 그렸기 떄문에 complete list로 넘긴다
            fingerPaths[id]?.computeBounds(pathBounds,true) // 영역 계산
            invalidate() // 다시 그려주기 위해 invalidate 실행
            fingerPaths[id] = null // 그림 이벤트가 끝났기 떄문에 다시 비워준다.
        }

        // 혹여 놓친 내용이 있는 경우 다시 확인해서 그림을 그려준다.
        for(i in 0 until cappedPointerCount){
            fingerPaths[i]?.let {
                val index = event.findPointerIndex(i)
                it.lineTo(event.getX(index),event.getY(index))
                it.computeBounds(pathBounds,true)
                invalidate()
            }
        }
        return true
    }

    private fun setPaint(){
        fingerPaint.apply {
            isAntiAlias = true
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 6f
            strokeCap = Paint.Cap.BUTT
        }
    }
}