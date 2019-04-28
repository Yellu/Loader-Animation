package com.animations.loaderAnimation

import `in`.animations.loader.R
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import com.agrawalsuneet.dotsloader.contracts.LoaderContract
import java.util.ArrayList

/**
 * Created by yellu on 28/03/19.
 */

class DotsLoader : LinearLayout, LoaderContract {
    internal var fadeOut1: ObjectAnimator? = null
    internal var fadeIn1:ObjectAnimator? = null
    internal var fadeOut2:ObjectAnimator? = null
    internal var fadeIn2:ObjectAnimator? = null
    internal var fadeOut3:ObjectAnimator? = null
    private var duration:Long = 1000L
    internal var fadeIn3:ObjectAnimator? = null
    internal var mAnimationSet: AnimatorSet? = null

    var noOfCircles: Int = 3
        set(value) {
            field = if (value < 1) 1 else value
        }

    var circleRadius: Int = 30
    var circleDistance: Int = 10

    var circleColor: Int = resources.getColor(android.R.color.holo_purple)

    private var calWidthHeight: Int = 0

    private lateinit var circlesList: ArrayList<CircleView>


    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttributes(attrs)
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttributes(attrs)
        initView()
    }

    constructor(context: Context, noOfCircles: Int, circleRadius: Int, circleDistance: Int, circleColor: Int, duration: Int) : super(context) {
        this.noOfCircles = noOfCircles
        this.circleRadius = circleRadius
        this.circleDistance = circleDistance
        this.circleColor = circleColor
        this.duration = duration.toLong()

        initView()
    }

    override fun initAttributes(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DotsLoader, 0, 0)

        noOfCircles = typedArray.getInteger(R.styleable.DotsLoader_lights_noOfCircles, 3)

        circleRadius = typedArray.getDimensionPixelSize(R.styleable.DotsLoader_lights_circleRadius, 30)
        circleDistance = typedArray.getDimensionPixelSize(R.styleable.DotsLoader_lights_circleDistance, 10)

        circleColor = typedArray.getColor(R.styleable.DotsLoader_lights_circleColor,
                resources.getColor(android.R.color.holo_blue_dark
                ))

        duration = typedArray.getInteger(R.styleable.DotsLoader_light_duration, 1000).toLong()

        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (calWidthHeight == 0) {
            calWidthHeight = (2 * circleRadius * noOfCircles) + ((noOfCircles - 1) * circleDistance)
        }

        setMeasuredDimension(calWidthHeight, calWidthHeight)
    }


    private fun initView() {
        removeAllViews()
        removeAllViewsInLayout()

        circlesList = ArrayList()

        if (calWidthHeight == 0) {
            calWidthHeight = (2 * circleRadius * noOfCircles) + ((noOfCircles - 1) * circleDistance)
        }

        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        linearLayout.layoutParams = params

        for (countJ in 0 until noOfCircles) {
            val circleView = CircleView(context, circleRadius, circleColor)

            val innerParam = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            if (countJ != 0) {
                innerParam.leftMargin = circleDistance
            }

            linearLayout.addView(circleView, innerParam)
            circlesList.add(circleView)
        }

        addView(linearLayout)


        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                    startAnimation()
                this@DotsLoader.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun startAnimation() {
        fadeOut1 = ObjectAnimator.ofFloat(circlesList[0], "alpha", 1f, .0f).setDuration(duration)
        fadeIn1 = ObjectAnimator.ofFloat(circlesList[0], "alpha", .0f, 1f).setDuration(duration)

        fadeOut2 = ObjectAnimator.ofFloat(circlesList[1], "alpha", 1f, .0f).setDuration(duration)
        fadeIn2 = ObjectAnimator.ofFloat(circlesList[1], "alpha", .0f, 1f).setDuration(duration)

        fadeOut3 = ObjectAnimator.ofFloat(circlesList[2], "alpha", 1f, .0f).setDuration(duration)
        fadeIn3 = ObjectAnimator.ofFloat(circlesList[2], "alpha", .0f, 1f).setDuration(duration)

        mAnimationSet = AnimatorSet()
        mAnimationSet!!.playSequentially(fadeIn1, fadeIn2, fadeIn3, fadeOut1, fadeOut2, fadeOut3)

        mAnimationSet!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                    mAnimationSet!!.start()
            }
        })
        mAnimationSet!!.start()
    }
}

