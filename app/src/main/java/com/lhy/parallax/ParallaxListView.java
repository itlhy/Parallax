package com.lhy.parallax;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * 创 建 人: 路好营
 * 创建日期: 2017/3/30 11:55
 * 添加备注: 视差特效ListView
 */

public class ParallaxListView extends ListView {
    private ImageView ivHeader;
    private int ivHeaderHeight;
    private int drawableHeight;

    public ParallaxListView(Context context) {
        this(context, null);
    }

    public ParallaxListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParallaxListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 滑动到ListView头部和底部的时候才会执行
     *
     * @param deltaX
     * @param deltaY         竖直方向滑动的瞬时变化量, 顶部下拉为- , 底部上拉为+
     * @param scrollX
     * @param scrollY        竖直方向的滑动超出的距离, 顶部为-, 底部为+
     * @param scrollRangeX
     * @param scrollRangeY   竖直方向滑动的范围
     * @param maxOverScrollX
     * @param maxOverScrollY 竖直方向最大的滑动位置
     * @param isTouchEvent   是否是用户触摸拉动 , true表示用户手指触摸拉动, false 是惯性
     * @return
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        if (deltaY < 0 && isTouchEvent) {//用户触摸并顶部下拉
            int newHeight = ivHeader.getHeight() + Math.abs(deltaY / 3);//修改背景图片的显示高度
            if (newHeight <= drawableHeight) {//范围不能超过背景图片的原始高度
                ivHeader.getLayoutParams().height = newHeight;//赋值背景图片的新的显示高度
                ivHeader.requestLayout();//再次绘制
            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    public void setParallaxImage(ImageView ivHeader) {
        this.ivHeader = ivHeader;
        ivHeaderHeight = ivHeader.getHeight();//留给背景图片的默认显示高度
        drawableHeight = ivHeader.getDrawable().getIntrinsicHeight();//背景图片的原始高度
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP://松手回弹,把当前头布局的高度currentHeight恢复到初始默认高度ivHeaderHeight
                int currentHeight = ivHeader.getHeight();
                ValueAnimator valueAnimator = ValueAnimator.ofInt(currentHeight, ivHeaderHeight);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float fraction = animation.getAnimatedFraction();//动画执行过程中不断变化的分度值 0 --> 1
                        Integer animatedValue = (Integer) animation.getAnimatedValue();
                        ivHeader.getLayoutParams().height = animatedValue;
                        ivHeader.requestLayout();
                    }
                });
                valueAnimator.setInterpolator(new OvershootInterpolator(2));//插补器,回弹动画
                valueAnimator.setDuration(300);
                valueAnimator.start();
                break;
        }
        return super.onTouchEvent(ev);
    }
}
