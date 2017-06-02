package com.paulyung.laybellayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yang on 2016/11/7.
 * paulyung@outlook.com
 * a auto new line layout
 *
 * @attr int android.R.styleable#LaybelLayout_line_padding
 * @attr int android.R.styleable#LaybelLayout_child_margin
 * @attr ref android.R.styleable#LaybelLayout_text_background
 * <p>
 * the default child view is TextView
 */

public class LaybelLayout extends ViewGroup implements View.OnClickListener{
    private static final String TAG = "LaybelLayout";

    private List<View> mChildView;
    private Map<View, ChildLayoutMsg> mChildrenMsg;
    private int mLinePadding;//行内上下边距
    private int minWidth, minHeight;//本控件的最小宽高

    private OnItemClickListener onItemClickListener;

    //------------ child view msg ---------------
    private int childMargin;
    private int textBackground;

    //------------ child view data --------------
    private Adapter mAdapter;

    public LaybelLayout(Context context) {
        this(context, null);
    }

    public LaybelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LaybelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mChildView = new ArrayList<>();
        mChildrenMsg = new HashMap<>();
        initAttr(attrs);
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray t = getContext().obtainStyledAttributes(attrs, R.styleable.LaybelLayout);
        mLinePadding = dip2px(t.getInt(R.styleable.LaybelLayout_line_padding, 0));
        childMargin = dip2px(t.getInt(R.styleable.LaybelLayout_child_margin, 0));
        textBackground = t.getResourceId(R.styleable.LaybelLayout_text_background, R.drawable.background);
        t.recycle();
    }

    /**
     * according to datas generate the child views
     */
    private void prepareView() {
        if (mAdapter.getView() == null) {
            generateDefaultView();
        } else {
            for (int i = 0; i < mAdapter.getCount(); i++) {
                View child = mAdapter.getView();
                ViewGroup.MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
                if (params == null) {
                    params = (MarginLayoutParams) generateDefaultLayoutParams();
                }
                mAdapter.onDataSet(child, mAdapter.getItem(i));
                addView(child, params);
            }
        }
        if (onItemClickListener != null)
            for (int i = 0; i < getChildCount(); i++) {
                getChildAt(i).setOnClickListener(this);
            }
    }

    private void generateDefaultView() {
        for (int i = 0; i < mAdapter.getCount(); ++i) {
            TextView child = new TextView(getContext());
            ViewGroup.MarginLayoutParams params = (MarginLayoutParams) generateDefaultLayoutParams();
            params.leftMargin = dip2px(5);
            params.rightMargin = dip2px(5);
            child.setBackgroundDrawable(getContext().getResources().getDrawable(textBackground));
            child.setText(mAdapter.getItem(i));
            mAdapter.onDataSet(child, mAdapter.getItem(i));
            addView(child, params);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        prepareView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        minWidth = getPaddingLeft() + getPaddingRight();
        minHeight = getPaddingTop() + getPaddingBottom();
        int count = getChildCount();
        for (int i = 0; i < count; ++i) {
            View child = getChildAt(i);
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            //如果单个View和本控件的padding加起来超过本控件的宽度，则让它的宽度 <= 本控件宽度 - Padding - margin
            int defSize = getPaddingLeft() + layoutParams.leftMargin
                    + child.getMeasuredWidth() + layoutParams.rightMargin + getPaddingRight();
            if (defSize > getMeasuredWidth()) {
                defSize = getMeasuredWidth() - layoutParams.leftMargin
                        - layoutParams.rightMargin - getPaddingLeft() - getPaddingRight();
                int widthSpec = MeasureSpec.makeMeasureSpec(defSize, MeasureSpec.AT_MOST);

                //根据measureChildWithMargins里面获取高度 Spec 的方式，重新获取到高度的Spec
                int heightSpec = getChildMeasureSpec(heightMeasureSpec,
                        getPaddingTop() + getPaddingBottom() + layoutParams.topMargin
                                + layoutParams.bottomMargin, layoutParams.height);
                child.measure(widthSpec, heightSpec);
            }
            if (!mChildView.contains(child))
                mChildView.add(child);
        }
        writeViewMsg();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY)
            setMeasuredDimension(minWidth, minHeight);
        else if (widthMode != MeasureSpec.EXACTLY)
            setMeasuredDimension(minWidth, getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
        else if (heightMode != MeasureSpec.EXACTLY)
            setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), minHeight);
    }

    private void writeViewMsg() {
        int lineHeight = 0;//单行高度
        int lineHeightSum = 0;//前面总高度
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;
        int freeWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();//横向剩余空间
        boolean isFirst = true;//是否是某一行的第一个
        int tmpWidth = 0;

        for (int i = 0; i < mChildView.size(); i++) {
            View view = mChildView.get(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();

            //判断剩余空间是否够用，如果不够，则进入下一行
            int childWidth = layoutParams.leftMargin + view.getMeasuredWidth() +
                    layoutParams.rightMargin;
            if (childWidth > freeWidth) {
                isFirst = true;
                lineHeightSum += lineHeight;
                lineHeight = 0;
                freeWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();//设为初始剩余
            }

            if (isFirst) {
                left = getPaddingLeft() + layoutParams.leftMargin;
                isFirst = false;
                if (tmpWidth > minWidth)
                    minWidth = tmpWidth;
                tmpWidth = childWidth;
            } else {
                View prView = mChildView.get(i - 1);
                MarginLayoutParams ll = (MarginLayoutParams) prView.getLayoutParams();
                left += prView.getMeasuredWidth() + ll.rightMargin + layoutParams.leftMargin;
                tmpWidth += childWidth;
            }
            top = getPaddingTop() + lineHeightSum + mLinePadding + layoutParams.topMargin;
            right = left + view.getMeasuredWidth();
            bottom = top + view.getMeasuredHeight();
            int tmpHeight = mLinePadding * 2
                    + layoutParams.topMargin
                    + view.getMeasuredHeight()
                    + layoutParams.bottomMargin;
            if (tmpHeight > lineHeight)//选出一行当中占用高度最多的作为行高
                lineHeight = tmpHeight;
            freeWidth -= childWidth;

            if (mChildrenMsg.containsKey(view))
                mChildrenMsg.remove(view);
            mChildrenMsg.put(view, new ChildLayoutMsg(left, top, right, bottom));
        }
        lineHeightSum += lineHeight;//加上最后一行的高度
        minHeight += lineHeightSum;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Set<View> set = mChildrenMsg.keySet();
        for (View child : set) {
            ChildLayoutMsg msg = mChildrenMsg.get(child);
            child.layout(msg.left, msg.top, msg.right, msg.bottom);
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(super.generateDefaultLayoutParams());
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(mChildView.indexOf(v));
        }
    }

    private static final class ChildLayoutMsg {
        ChildLayoutMsg(int l, int t, int r, int b) {
            left = l;
            top = t;
            right = r;
            bottom = b;
        }

        int left;
        int right;
        int top;
        int bottom;
    }

    public void setLinePadding(int paddingSize) {
        mLinePadding = dip2px(paddingSize);
    }

    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
    }

    public static class Adapter {
        private List<String> mDatas;

        public Adapter(List<String> datas) {
            mDatas = datas;
        }

        public Adapter(String... datas) {
            mDatas = Arrays.asList(datas);
        }

        public List<String> getDatas() {
            return mDatas;
        }

        public int getCount() {
            if (mDatas == null)
                return 0;
            return mDatas.size();
        }

        public String getItem(int position) {
            if (mDatas == null)
                return "";
            return mDatas.get(position);
        }

        /**
         * if you want to use custom child view, you can overide this method,
         * otherwise,the default view can be set
         *
         * @return your custom view
         */
        public View getView() {
            return null;
        }

        //called when data set by LaybelLayout
        public void onDataSet(View v, String data) {
        }
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        onItemClickListener = l;
    }

    public interface OnItemClickListener {
        void onItemClick(int p);
    }
}
