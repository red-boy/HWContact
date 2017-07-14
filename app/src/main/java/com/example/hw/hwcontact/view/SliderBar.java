package com.example.hw.hwcontact.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.hw.hwcontact.util.DpUtil;

/**
 * Created by HaodaHw on 2017/6/22.
 * 自定义控件
 */

public class SliderBar extends View {
    private String indexStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#";
    private final int TOTAL_MARGIN = 120;//距离顶部的总高度
    private final int TOP_MARGIN = 60;//画字母时距离
    private Context mContext;
    private Paint mPaint;
    private int singleHeight;//每个字母的高度
    private int mHeight;
    private int mWidth;

    private indexChangeListen mListen;


    public SliderBar(Context context) {
        this(context, null);
    }

    public SliderBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SliderBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(Color.GRAY);
        mPaint.setTextSize(35);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = (int) (h - DpUtil.dp2px(mContext, TOTAL_MARGIN));
        mWidth = w;

        singleHeight = mHeight / indexStr.length();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < indexStr.length(); i++) {
            String stringTag = indexStr.substring(i, i + 1);
            canvas.drawText(stringTag, mWidth / 2, singleHeight * (i + 1) + DpUtil.dp2px(mContext, TOP_MARGIN), mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPaint.setColor(Color.BLACK);//控件按下时变黑色
                postInvalidate();//刷新
                break;
            case MotionEvent.ACTION_MOVE:
                //滑动 event.getY()得到在父View中的Y坐标，通过和总高度的比例再乘以字符个数总长度得到按下的位置
                //getX()和getY()获取到的是相对于当前View左上角的坐标,而getRawX和getRawY()获取的是相对于屏幕左上角的坐标。
                int position = (int) ((event.getY() - getTop() - DpUtil.dp2px(mContext, TOP_MARGIN)) / mHeight * indexStr.toCharArray().length);
                if (position >= 0 && position < indexStr.length()) {
                    ((IndexBar) getParent()).setDrawData(event.getY(), String.valueOf(indexStr.toCharArray()[position]), position);//获取父控件
                    if (mListen != null) {
                        mListen.indexChange(indexStr.substring(position, position + 1));//监听接口传值
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                ((IndexBar) getParent()).setTagStatus(false);//获取父控件，并调用方法
                mPaint.setColor(Color.GRAY);
                postInvalidate();
                break;
        }


        return true;
    }

    public void setIndexStr(String indexStr) {
        this.indexStr = indexStr;
    }

    public interface indexChangeListen {
        void indexChange(String tag);
    }

    public void setindexChangeListen(indexChangeListen listen) {
        mListen = listen;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
