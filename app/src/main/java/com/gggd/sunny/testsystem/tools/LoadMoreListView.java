package com.gggd.sunny.testsystem.tools;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoadMoreListView extends ListView {

    private RelativeLayout mFooterView;
    private LinearLayout layout;
    private ProgressBar progressBar;
    private TextView textView;
    private boolean mIsLoadingMore = false;
    private boolean mHasMore = true;

    private LoadMoreListener loadMoreListener;
    private OnCustomScrollListener onCustomScrollListener;


    public LoadMoreListView(Context context) {
        super(context);
        initlize(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initlize(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initlize(context);
    }

    private void initlize(Context context) {
        initView(context);
        initListener(context);
        //添加footerview
        addFooterView(mFooterView);
        hideLoadingView();
    }

    private void initView(Context context) {

        mFooterView = new RelativeLayout(context);
        RelativeLayout.LayoutParams rParams;
        LinearLayout.LayoutParams lParams;

        rParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        mFooterView.addView(layout, rParams);

        lParams = new LinearLayout.LayoutParams(60, 60);
        lParams.weight = Gravity.CENTER_VERTICAL;
        progressBar = new ProgressBar(context);
        layout.addView(progressBar, lParams);

        lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParams.leftMargin = 10;
        textView = new TextView(context);
        textView.setText("正在加载...");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        layout.addView(textView, lParams);
    }


    private void initListener(Context context) {
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (onCustomScrollListener != null) {
                    onCustomScrollListener.onScrollStateChanged(view, scrollState);
                }


            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (onCustomScrollListener != null) {
                    onCustomScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
                if (mFooterView.getParent() != null && !mIsLoadingMore && mHasMore) {
                    layout.setVisibility(View.VISIBLE);
                    mFooterView.setVisibility(View.VISIBLE);
                    if (loadMoreListener != null) {
                        loadMoreListener.loadMore();
                    }
                    showLoadingView();
                }

            }
        });
    }


    /**
     * 设置OnCustomScrollListener回调
     *
     * @param listener
     */
    public void setOnCustomScrollListener(OnCustomScrollListener listener) {
        this.onCustomScrollListener = listener;
    }

    /**
     * OnScrollListener的回调接口
     */
    public interface OnCustomScrollListener {
        void onScrollStateChanged(AbsListView view, int scrollState);

        void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    }

    /**
     * 设置加载更多回调接口
     *
     * @param listener
     */
    public void setLoadMoreListener(LoadMoreListener listener) {
        this.loadMoreListener = listener;
    }

    /**
     * 加载更多回调接口
     */
    public interface LoadMoreListener {
        void loadMore();
    }

    /**
     * 是否还有更多
     *
     * @param hasMore
     */
    public void setHasMore(boolean hasMore) {
        mHasMore = hasMore;
    }

    /**
     * 隐藏加载loading
     */
    private void hideLoadingView() {
        progressBar.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        mFooterView.setVisibility(GONE);
    }

    /**
     * 显示loading
     */
    private void showLoadingView() {
        progressBar.setVisibility(View.VISIBLE);
        layout.setVisibility(View.VISIBLE);
        mFooterView.setVisibility(View.VISIBLE);
    }

    /**
     * 加载更多结束
     * 加载完成后调用
     */
    public void loadMoreFinish() {
        mIsLoadingMore = false;
        hideLoadingView();
    }

    /**
     * 正在加载更多
     * 加载中调用
     */
    public void isLoadingMore() {
        mIsLoadingMore = true;
        showLoadingView();
    }


}