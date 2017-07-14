package com.example.hw.hwcontact.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.hw.hwcontact.util.ColorUtil;

/**
 * Created by HaodaHw on 2017/6/22.
 * <p>
 * 该自定义控件内部包含SideBar
 */

public class IndexBar extends ViewGroup {
    private boolean isShowTag;
    private Context mContext;
    private int childWidth;
    private int position;//位置用于改变字体颜色
    private Paint mPaint;
    private float centerY;
    private String tag = "";
    private int mHeight;
    private int mWidth;
    private float circleRadius = 100;

    public IndexBar(Context context) {
        this(context, null);
    }

    public IndexBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        setWillNotDraw(false);//若想要重写onDraw()
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//防锯齿
        mPaint.setDither(true);//防抖动
        mPaint.setColor(Color.RED);
    }

    //当该组件的大小被改变时回调该方法.
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
    }

    //调用该方法来检测View组件及它所包含的所有子组件的大小.
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);//从父容器传递给子容器的布局需求(宽/高)

        int wMode = MeasureSpec.getMode(widthMeasureSpec);//MeasureSpec 这是一个含mode和size的结合体 参数widthMeasureSpec提供view的水平空间的规格说明
        int wSize = MeasureSpec.getSize(widthMeasureSpec);//得到尺寸
        int hMode = MeasureSpec.getMode(heightMeasureSpec);//得到模式
        int hSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (wMode) {
            case MeasureSpec.EXACTLY://父view强加给子view确切的值
                mWidth = wSize;
                break;
            case MeasureSpec.AT_MOST:
                mWidth = wSize;
                break;
            case MeasureSpec.UNSPECIFIED://未加规定的
                break;
        }
        switch (hMode) {
            case MeasureSpec.EXACTLY:
                mHeight = hSize;
                break;
            case MeasureSpec.AT_MOST:
                mHeight = hSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        setMeasuredDimension(mWidth, mHeight);//onMeasure里必须调用该方法来保存测量结果
    }

    //自定义控件继承自ViewGroup都得重写该方法，因为要布局各个子控件的位置
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childNum = getChildCount();
        if (childNum <= 0) return;

        View childView = getChildAt(0);
        childWidth = childView.getMeasuredWidth();//原始的大小
        //把SideBar排列到最右侧
        childView.layout((mWidth - childWidth), 0, mWidth, mHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isShowTag) {
            ColorUtil.setPaintColor(mPaint, position);//根据位置来不断变换Paint的颜色
            //下面陆续绘制圆和字
            canvas.drawCircle((mWidth - childWidth) / 2, centerY, circleRadius, mPaint);
            mPaint.setColor(Color.WHITE);
            mPaint.setTextSize(80);
            canvas.drawText(tag, (mWidth - childWidth - mPaint.measureText(tag)) / 2, centerY - (mPaint.ascent() + mPaint.descent()) / 2, mPaint);
        }
    }

    /**
     * @param centerY  要绘制的圆的Y坐标
     * @param tag      要绘制的字母Tag
     * @param position 字母Tag所在位置,用于循环设置绘制的资颜色
     */
    public void setDrawData(float centerY, String tag, int position) {
        this.position = position;
        this.centerY = centerY;
        this.tag = tag;
        isShowTag = true;
        postInvalidate();
    }

    /**
     * 通过标志位来控制是否来显示圆
     *
     * @param isShowTag 是否显示圆
     */
    public void setTagStatus(boolean isShowTag) {
        this.isShowTag = isShowTag;
        postInvalidate();
    }
}
