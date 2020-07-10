package com.zhboy.ycwwz.base_library.utils

import android.animation.*
import android.view.View
import android.view.animation.*
import android.widget.ImageView
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import com.zhboy.ycwwz.base_library.R


/**
 * 自定义动画类
 */
object CustomAnimatorUtils {

    /**
     * 抖动动画 X轴方向抖动
     */
    fun shakeAnimatorX(view: View): ObjectAnimator {
        val delta = view.resources.getDimensionPixelOffset(R.dimen.dp_8)

        val pvhTranslateX = PropertyValuesHolder.ofKeyframe(
            View.TRANSLATION_X,
            Keyframe.ofFloat(0f, 0f),
            Keyframe.ofFloat(.10f, (-delta).toFloat()),
            Keyframe.ofFloat(.26f, delta.toFloat()),
            Keyframe.ofFloat(.42f, (-delta).toFloat()),
            Keyframe.ofFloat(.58f, delta.toFloat()),
            Keyframe.ofFloat(.74f, (-delta).toFloat()),
            Keyframe.ofFloat(.90f, delta.toFloat()),
            Keyframe.ofFloat(1f, 0f)
        )
        return ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX).setDuration(1000)
    }


    /**
     * 抖动动画 Y轴方向抖动
     */
    fun shakeAnimatorY(view: View): ObjectAnimator {
        val delta = view.resources.getDimensionPixelOffset(R.dimen.dp_8)

        val pvhTranslateX = PropertyValuesHolder.ofKeyframe(
            View.TRANSLATION_Y,
            Keyframe.ofFloat(0f, 0f),
            Keyframe.ofFloat(.10f, (-delta).toFloat()),
            Keyframe.ofFloat(.26f, delta.toFloat()),
            Keyframe.ofFloat(.42f, (-delta).toFloat()),
            Keyframe.ofFloat(.58f, delta.toFloat()),
            Keyframe.ofFloat(.74f, (-delta).toFloat()),
            Keyframe.ofFloat(.90f, delta.toFloat()),
            Keyframe.ofFloat(1f, 0f)
        )
        return ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX).setDuration(1000)
    }



    /**
     * 抖动动画 放大
     */
    fun shakeAnimator(view: View): ObjectAnimator {
        return shakeAnimator(view, 1f)
    }

    private fun shakeAnimator(view: View, shakeFactor: Float): ObjectAnimator {

        val pvhScaleX = PropertyValuesHolder.ofKeyframe(
            View.SCALE_X,
            Keyframe.ofFloat(0f, 1f),
            Keyframe.ofFloat(.1f, .9f),
            Keyframe.ofFloat(.2f, .9f),
            Keyframe.ofFloat(.3f, 1.1f),
            Keyframe.ofFloat(.4f, 1.1f),
            Keyframe.ofFloat(.5f, 1.1f),
            Keyframe.ofFloat(.6f, 1.1f),
            Keyframe.ofFloat(.7f, 1.1f),
            Keyframe.ofFloat(.8f, 1.1f),
            Keyframe.ofFloat(.9f, 1.1f),
            Keyframe.ofFloat(1f, 1f)
        )

        val pvhScaleY = PropertyValuesHolder.ofKeyframe(
            View.SCALE_Y,
            Keyframe.ofFloat(0f, 1f),
            Keyframe.ofFloat(.1f, .9f),
            Keyframe.ofFloat(.2f, .9f),
            Keyframe.ofFloat(.3f, 1.1f),
            Keyframe.ofFloat(.4f, 1.1f),
            Keyframe.ofFloat(.5f, 1.1f),
            Keyframe.ofFloat(.6f, 1.1f),
            Keyframe.ofFloat(.7f, 1.1f),
            Keyframe.ofFloat(.8f, 1.1f),
            Keyframe.ofFloat(.9f, 1.1f),
            Keyframe.ofFloat(1f, 1f)
        )

        val pvhRotate = PropertyValuesHolder.ofKeyframe(
            View.ROTATION,
            Keyframe.ofFloat(0f, 0f),
            Keyframe.ofFloat(.1f, -3f * shakeFactor),
            Keyframe.ofFloat(.2f, -3f * shakeFactor),
            Keyframe.ofFloat(.3f, 3f * shakeFactor),
            Keyframe.ofFloat(.4f, -3f * shakeFactor),
            Keyframe.ofFloat(.5f, 3f * shakeFactor),
            Keyframe.ofFloat(.6f, -3f * shakeFactor),
            Keyframe.ofFloat(.7f, 3f * shakeFactor),
            Keyframe.ofFloat(.8f, -3f * shakeFactor),
            Keyframe.ofFloat(.9f, 3f * shakeFactor),
            Keyframe.ofFloat(1f, 0f)
        )

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY, pvhRotate)
            .setDuration(1200)
    }

    private fun fadeIn(view: View, startAlpha: Float, endAlpha: Float, duration: Long) {
        if (view.visibility == View.VISIBLE) return

        view.visibility = View.VISIBLE
        val animation = AlphaAnimation(startAlpha, endAlpha)
        animation.duration = duration
        view.startAnimation(animation)
    }

    /**
     * 闪动的动画
     */
    fun scaleShandong(view: View?, repeatCount: Int) {
        val animation = ScaleAnimation(
            1f,
            1.2f,
            1f,
            1.2f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        animation.duration = 500
        animation.repeatCount = repeatCount
        animation.repeatMode = Animation.REVERSE
        view?.startAnimation(animation)
    }

    /**
     * 闪动的动画
     */
    fun scaleShandongO(view: View?, repeatCount: Int): ScaleAnimation {
        val animation = ScaleAnimation(
            1f,
            1.2f,
            1f,
            1.2f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        animation.duration = 500
        animation.repeatCount = repeatCount
        animation.repeatMode = Animation.REVERSE
        view?.startAnimation(animation)
        return animation
    }

    /**
     * 闪动的动画
     */
    fun scaleShandongO1(view: View?, repeatCount: Int): ScaleAnimation {
        val animation = ScaleAnimation(
            0.8f,
            1f,
            0.8f,
            1f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        animation.duration = 500
        animation.repeatCount = repeatCount
        animation.repeatMode = Animation.REVERSE
        view?.startAnimation(animation)
        return animation
    }

    /**
     * 中心点放大缩小动画
     */
    fun scaleXY(view: View?, time: Long) {
        view?.let {
            val objectAnimator1 =
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f)
            val objectAnimator2 =
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f)
            val objectAnimator3 = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)

            val animatorSet = AnimatorSet()
            animatorSet.play(objectAnimator1).with(objectAnimator2).with(objectAnimator3)
            animatorSet.duration = time

            view.visibility = View.VISIBLE
            animatorSet.start()
        }
    }

    /**
     * 中心点放大缩小动画
     */
    fun scaleXY(view: View?, time: Long, scaleX: Float, scaleY: Float): AnimatorSet {

        val objectAnimator1 =
            ObjectAnimator.ofFloat(view, "scaleX", 0f, scaleX)
        val objectAnimator2 =
            ObjectAnimator.ofFloat(view, "scaleY", 0f, scaleY)
        val objectAnimator3 = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)

        val animatorSet = AnimatorSet()
        animatorSet.play(objectAnimator1).with(objectAnimator2).with(objectAnimator3)
        animatorSet.duration = time
        animatorSet.start()
        return animatorSet

    }


    /**
     * 抽奖页财神收起动画
     */
    fun scaleXYToRight(view: View?, caiShenView: ImageView): AnimatorSet {
        val animatorSet = AnimatorSet()
        view?.let {
            caiShenView.isEnabled = false
            //往右平移
            val animator = ObjectAnimator.ofFloat(
                view, "translationX",
                0f, 10f, 30f, 40f, 50f, 60f, 70f, 80f, 90f, 100f, 500f, 1000f
            )
            animator.duration = 300//动画时间
            //animator.setInterpolator( BounceInterpolator());//实现反复移动的效果
            animator.repeatCount = 0//设置动画重复次数

            //往右平移
            val animator2 = ObjectAnimator.ofFloat(
                caiShenView, "translationX",
                0f, 10f, 30f, 40f, 50f, 60f, 70f, 80f
            )
            animator2.duration = 500//动画时间
            animator2.repeatCount = 0//设置动画重复次数

            //抖动一下
            val animator3 = ObjectAnimator.ofFloat(
                caiShenView, "scaleX",
                1f, 1.1f, 1f
            )
            animator3.duration = 300//动画时间
            animator3.repeatCount = 1//设置动画重复次数


//            animatorSet.play(animator3).with(animator4).after(animator).with(animator2)
            animatorSet.play(animator).with(animator2).before(animator3)
            animatorSet.start()
            animator3.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    view.visibility = View.INVISIBLE
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {

                }

            })

        }
        return animatorSet
    }

    /**
     * 抽奖页财神唤醒出来动画
     */
    fun scaleXYFromRight(view: View?, caiShenView: ImageView): AnimatorSet {
        val animatorSet = AnimatorSet()
        view?.let {
            //往左平移
            val animator = ObjectAnimator.ofFloat(
                view, "translationX",
                600f, 500f, 300f, 200f, 150f, 0f
            )
            animator.duration = 400//动画时间
            //animator.setInterpolator( BounceInterpolator());//实现反复移动的效果
            animator.repeatCount = 0//设置动画重复次数

            //往左平移
            val animator2 = ObjectAnimator.ofFloat(
                caiShenView, "translationX",
                80f, 70f, 60f, 50f, 40f, 0f
            )
            animator2.duration = 500//动画时间
            animator2.repeatCount = 0//设置动画重复次数

            //抖动一下
            val animator3 = ObjectAnimator.ofFloat(
                view, "scaleX",
                1f, 1.1f, 1f
            )
            animator3.duration = 300//动画时间
            animator3.repeatCount = 1//设置动画重复次数

            animatorSet.play(animator).with(animator2).before(animator3)
            animatorSet.start()
            view.visibility = View.VISIBLE
        }
        return animatorSet
    }

    @SuppressLint("ObjectAnimatorBinding")
    fun getInAnim(): Animator {
        val trX = PropertyValuesHolder.ofFloat("translationX", 300f, 0f)
        val trY = PropertyValuesHolder.ofFloat("translationY", 0f, 0f)
        val trAlpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f)
        return ObjectAnimator.ofPropertyValuesHolder(this, trY, trAlpha, trX)
    }

    @SuppressLint("ObjectAnimatorBinding")
    fun getOutAnim(): Animator {
        val trX = PropertyValuesHolder.ofFloat("translationX", 0f, -300f)
        val trY = PropertyValuesHolder.ofFloat("translationY", 0f, 0f)
        val trAlpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f)
        return ObjectAnimator.ofPropertyValuesHolder(this, trY, trAlpha, trX)
    }


    /**
     * 高度放大
     */
    fun scaleY(view: View?, time: Long, height: Int) {
        view?.visibility = View.VISIBLE
        val scaleY = ValueAnimator.ofInt(0, height) ////第二个高度 需要注意一下, 因为view默认是GONE  无法直接获取高度
        scaleY.addUpdateListener { animation ->
            val animatorValue = (animation.animatedValue) as Int
            val params = view?.layoutParams
            params?.height = animatorValue
            view?.layoutParams = params
        }
        scaleY.setTarget(view)
        scaleY.duration = time
        scaleY.start()
    }

    /**
     * 高度收缩
     */
    fun scaleYToSmall(view: View?, time: Long, height: Int) {

        val scaleY = ValueAnimator.ofInt(height, 0) ////第二个高度 需要注意一下, 因为view默认是GONE  无法直接获取高度
        scaleY.addUpdateListener { animation ->
            val animatorValue = (animation.animatedValue) as Int
            val params = view?.layoutParams
            params?.height = animatorValue
            view?.layoutParams = params
        }
        scaleY.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                view?.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })
        scaleY.setTarget(view)
        scaleY.duration = time
        scaleY.start()
    }


    //只抖动
    fun shakeAnimatorWithOutScale(view: View, shakeFactor: Float=1f): ObjectAnimator{
        val pvhRotate = PropertyValuesHolder.ofKeyframe(
            View.ROTATION,
            Keyframe.ofFloat(0f, 0f),
            Keyframe.ofFloat(.1f, -3f * shakeFactor),
            Keyframe.ofFloat(.2f, 3f * shakeFactor),
            Keyframe.ofFloat(.3f, -3f * shakeFactor),
            Keyframe.ofFloat(.4f, 3f * shakeFactor),
            Keyframe.ofFloat(.5f, -3f * shakeFactor),
            Keyframe.ofFloat(.6f, 3f * shakeFactor),
            Keyframe.ofFloat(.7f, -3f * shakeFactor),
            Keyframe.ofFloat(.8f, 3f * shakeFactor),
            Keyframe.ofFloat(.9f, -3f * shakeFactor),
            Keyframe.ofFloat(1f, 0f)
        )

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhRotate)
            .setDuration(1200)
    }
}