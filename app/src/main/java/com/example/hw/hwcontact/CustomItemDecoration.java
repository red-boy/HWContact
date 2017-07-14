package com.example.hw.hwcontact;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.example.hw.hwcontact.bean.ContactBean;
import com.example.hw.hwcontact.util.ColorUtil;
import com.example.hw.hwcontact.util.DpUtil;

import java.util.List;

/**
 * Created by HaodaHw on 2017/6/22.
 * <p>
 * ItemDecoration类主要是三个方法
 */

public class CustomItemDecoration extends RecyclerView.ItemDecoration {
    private static final int dividerHeight = 80;
    private Rect mBounds = new Rect();
    private List<ContactBean> mList;
    private Context mContext;
    private String tagsStr;
    private Paint mPaint;

    public CustomItemDecoration(Context context) {
        mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setDatas(List<ContactBean> list, String tagsStr) {
        this.mList = list;
        this.tagsStr = tagsStr;
    }

    /**
     * 2.结合getItemOffsets方法按需绘制ItemView上面的分类Tag
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null) {
            return;
        }
        c.save();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            int position = layoutParams.getViewLayoutPosition();
            if (mList == null || mList.size() == 0 || mList.size() <= position || position < 0) {
                continue;
            }

            if (position == 0) {
                //第一条数据有bar
                drawTitleBar(c, parent, child, mList.get(position), tagsStr.indexOf(mList.get(position).getIndexTag()));
            } else if (position > 0) {
                if (TextUtils.isEmpty(mList.get(position).getIndexTag())) {
                    continue;
                }
                //与上一条数据中的tag不同时，该显示bar了
                if (!mList.get(position).getIndexTag().equals(mList.get(position - 1).getIndexTag())) {
                    drawTitleBar(c, parent, child, mList.get(position), tagsStr.indexOf(mList.get(position).getIndexTag()));
                }

            }
        }

        c.restore();
    }

    /**
     * 3.绘制顶部悬浮框
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //获取可视范围内的选项的头位置
        int position = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
        if (mList == null || mList.size() == 0 || mList.size() <= position || position < 0) {
            return;
        }
        final int bottom = parent.getPaddingTop() + dividerHeight;
        mPaint.setColor(Color.WHITE);
        c.drawRect(parent.getLeft(), parent.getPaddingTop(), parent.getRight() - parent.getPaddingRight(), parent.getPaddingTop() + dividerHeight,
                mPaint);
        ColorUtil.setPaintColor(mPaint, tagsStr.indexOf(mList.get(position).getIndexTag()));
        mPaint.setTextSize(40);
        c.drawCircle(DpUtil.dp2px(mContext, 42.5f), bottom - dividerHeight / 2, 35, mPaint);
        mPaint.setColor(Color.WHITE);
        c.drawText(mList.get(position).getIndexTag(), DpUtil.dp2px(mContext, 42.5f), bottom - dividerHeight / 3, mPaint);
    }

    /**
     * 1.用来设置每个ItemView的四个方向上的边距(撑开 ItemView 上、下、左、右四个方向的空间 )
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);

        if (mList == null || mList.size() == 0 || mList.size() <= position || position < 0) {
            super.getItemOffsets(outRect, view, parent, state);
            return;
        }

        if (position == 0) {
            outRect.set(0, dividerHeight, 0, 0);//设置itemview四个方向上的边距
        } else if (position > 0) {
            if (TextUtils.isEmpty(mList.get(position).getIndexTag())) {
                return;
            }
            if (!mList.get(position).getIndexTag().equals(mList.get(position - 1).getIndexTag())) { //与上一条数据中的tag不同时，该显示bar了
                outRect.set(0, dividerHeight, 0, 0);
            }
        }
    }

    /**
     * 绘制itemview上面的分类Tag
     *
     * @param canvas   Canvas
     * @param parent   RecyclerView
     * @param child    View
     * @param bean     ContactBean
     * @param position int 根据位置不断变换Tag的圆图形的颜色
     */
    private void drawTitleBar(Canvas canvas, RecyclerView parent, View child, ContactBean bean, int position) {
        int left = 0;
        int right = parent.getWidth();
        //返回一个包含Decoration和Margin在内的Rect
        parent.getDecoratedBoundsWithMargins(child, mBounds);

        int top = mBounds.top;
        int bottom = mBounds.top + Math.round(ViewCompat.getTranslationY(child)) + dividerHeight;
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(left, top, right, bottom, mPaint);//绘制矩形（Tag的区域范围）
        ColorUtil.setPaintColor(mPaint, position);//根据位置不断变换Paint的颜色
        mPaint.setTextSize(40);
        canvas.drawCircle(DpUtil.dp2px(mContext, 42.5f), bottom - dividerHeight / 2, 35, mPaint);//绘制Tag的圆图形
        mPaint.setColor(Color.WHITE);
        canvas.drawText(bean.getIndexTag(), DpUtil.dp2px(mContext, 42.5f), bottom - dividerHeight / 3, mPaint);//绘制Tag里的字母
    }
}
