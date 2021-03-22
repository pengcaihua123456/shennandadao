package test.yuedong.com.myapplication.recycleview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

import test.yuedong.com.myapplication.refreshlayout.TwinklingRefreshLayout;
import test.yuedong.com.myapplication.refreshlayout.inner.RefreshListenerAdapter;


/**
 * Created by virl on 15/8/3.
 */
public class RefreshLoadMoreRecyclerView extends TwinklingRefreshLayout {
    private static float MinVelocity = -1;
    private boolean isRefreshing;
    private boolean isLoading;
    private float _dy;

    public RefreshLoadMoreRecyclerView(Context context) {
        super(context);

        if (-1 == MinVelocity) {
            try {
                MinVelocity = ViewConfiguration.get(context.getApplicationContext()).getScaledTouchSlop();
            } catch (Throwable e) {
                MinVelocity = 120;
            }
        }

        init(context);
    }

    public RefreshLoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public boolean isScrollToTop() {
        if (recyclerView.getChildAt(0) != null) {
            if (recyclerView.getChildAt(0).getY() == 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private RecyclerView recyclerView;

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    private void init(Context context) {
        Log.e("peng", "RefreshLoadMoreRecyclerView----init");
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new ScrollSpeedLinearLayoutManager(context));
        recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event == null) {
                    return false;
                }
                Log.e("peng", "onTouch");
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        _dy = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE://區分點擊和滑動
                        float dy = event.getY();
                        float velocity = Math.abs(dy - _dy);
                        if (velocity >= MinVelocity) {
                            isRefreshing = false;
                            isLoading = false;
                        }
                        break;
                }
                if (!enableLoadmore) {
                    isLoading = false;
                }
                if (!enableRefresh) {
                    isRefreshing = false;
                }
                if (isRefreshing || isLoading) {
                    return true;
                }
                return false;
            }
        });
        addView(recyclerView, params);
    }

    public void setLayoutManager(LinearLayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void scrollToPosition(int position) {
        if (position < 0) {
            return;
        }
        recyclerView.smoothScrollToPosition(position);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    public void setRefreshable(boolean refreshAble) {
        super.setEnableRefresh(refreshAble);
    }

    public void setEnableLoadMore(boolean enable) {
        super.setEnableLoadmore(enable);
    }

    public void setLoadingMore(boolean loadingMore) {
        if (!loadingMore) {
            super.finishLoadmore();
        }
//        if(this.isLoadingMore == loadingMore) {
//            return;
//        }
//        isLoadingMore = loadingMore;
//        loadMoreView.setVisibility(loadingMore ? VISIBLE : INVISIBLE);
//        if(loadingMore) {
//            loadMoreCallBack.startLoadMore();
//        } else {
//            loadMoreCallBack.stopLoadMore();
//        }
        isLoading = loadingMore;
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        recyclerView.addItemDecoration(itemDecoration);
    }

    public void setOnRefreshListener(final OnRefeshDataListener onRefreshListener) {
        if (onRefreshListener != null) {
            setOnRefreshListener(new RefreshListenerAdapter() {
                @Override
                public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                    super.onRefresh(refreshLayout);
                    isRefreshing = true;
                    onRefreshListener.onRefeshData();
                }

                @Override
                public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                    super.onLoadMore(refreshLayout);
                    isLoading = true;
                    onRefreshListener.onLoadMoreData();
                }
            });
        }

//        if(onRefreshListener!=null){
////            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
////                @Override
////                public void onRefresh() {
////                    onRefreshListener.onRefeshData();
////                }
////            });
//            scrollPullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
//                @Override
//                public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//                    onRefreshListener.onRefeshData();
//                }
//
//                @Override
//                public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
//
//                }
//            });
//        }
    }

    public void setRefreshing(boolean refreshing) {
        if (!refreshing) {
            finishRefreshing();
        }
        isRefreshing = refreshing;
    }

//    private class MyOnScrollListener extends RecyclerView.OnScrollListener {
//
//
//        @Override
//        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//
//        }
//
//        @Override
//        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            super.onScrolled(recyclerView, dx, dy);
////            if (onScroll != null){
////                onScroll.onScrolled(recyclerView,dx,dy);
////            }
////            if(!loadMoreAble) {
////                return;
////            }
////
////            if (isLoadingMore) {
////                return;
////            }
////
////            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
////            int totalItemCount = layoutManager.getItemCount();
////
////            int lastPosition = layoutManager.findLastVisibleItemPosition();
////
////            if (!isLoadingMore && lastPosition >= totalItemCount - 1) {
////                loadMoreView.setVisibility(VISIBLE);
////                loadMoreCallBack.startLoadMore();
////                l.onLoadMore();
////                isLoadingMore = true;
////            }
//        }
//    }

    public void setOnScrollListener(final OnScrollListener onScroll) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (onScroll == null) {
                    return;
                }
                switch (newState) {
                    case 0:
                        onScroll.onScrollStop();
                        break;
                    case 1:
                    case 2:
                        onScroll.onScroll();
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                onScroll.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public void reset() {
        super.reset();
        if (recyclerView != null) {
            recyclerView.clearOnScrollListeners();
        }
    }

    public interface OnScrollListener {
        void onScroll();

        void onScrollStop();

        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }

    public interface LoadMoreViewCallBack {
        void startLoadMore();

        void stopLoadMore();
    }

    public interface OnRefeshDataListener {
        void onRefeshData();

        void onLoadMoreData();
    }

    // TODO:实现
    public interface OnLoadMoreListener {
        void onRefresh();

        void onLoadMore();
    }

    public void setLoadMoreView(View refreshView, View loadMoreView, Context context) {

    }

    public void setOnRefreshListener(OnLoadMoreListener listener) {

    }

    // TODO:原有方法注释掉了
    // public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener l)
    public void setOnSwipeRefreshListener(SwipeRefreshLayout.OnRefreshListener l) {
//        swipeRefreshLayout.setOnRefreshListener(l);
    }
    // TODO：end

    public void setLoadingText(String[] loadingText) {
        setTips(loadingText);
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    public boolean isLoading() {
        return isLoading;
    }
}
