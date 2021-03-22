package test.yuedong.com.myapplication.recycleview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by virl on 15/8/3.
 */
public abstract class LoadMoreAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    public static final int kTypeLoadMore = 1;

    public static final int kTypeCustomBegin = 100;
    private MyOnScrollListener onScrollListener;

    private boolean loadMoreAble = false;
    private boolean isLoadingMore = false;

    public void setLoadMoreAble(boolean enable) {
        this.loadMoreAble = enable;
    }

    private View loadMoreView;

    public void setLoadMoreView(View loadMoreView, RecyclerView recyclerView, OnLoadMoreListener listener) {
        if (null != onScrollListener) {
            recyclerView.removeOnScrollListener(onScrollListener);
        }
        this.loadMoreView = loadMoreView;
        onScrollListener = new MyOnScrollListener(listener);
        recyclerView.addOnScrollListener(onScrollListener);
    }

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType >= kTypeCustomBegin) {
            return createCustomViewHolder(parent, viewType);
        }
        if (viewType == kTypeLoadMore) {
            return getViewHolder(loadMoreView);
        }
        return null;
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        if (loadMoreAble && position == getAdapterItemCount()) {
            return;
        }
        adapterBindViewHolder(holder, position);
    }

    @Override
    public final int getItemViewType(int position) {
        if (loadMoreAble && position == getAdapterItemCount()) {
            return kTypeLoadMore;
        }
        return getAdapterItemViewType(position);
    }

    @Override
    public final int getItemCount() {
        if (loadMoreAble) {
            return getAdapterItemCount() + 1;
        }
        return getAdapterItemCount();
    }

    protected abstract void adapterBindViewHolder(VH holder, int position);

    protected abstract VH getViewHolder(View view);

    protected abstract VH createCustomViewHolder(ViewGroup parent, int viewType);

    protected abstract int getAdapterItemViewType(int position);

    protected abstract int getAdapterItemCount();

    private class MyOnScrollListener extends RecyclerView.OnScrollListener {
        OnLoadMoreListener l;
        int mVisibleItemCount;
        int mTotalItemCount;

        MyOnScrollListener(OnLoadMoreListener l) {
            this.l = l;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            mVisibleItemCount = layoutManager.getChildCount();
            mTotalItemCount = layoutManager.getItemCount();

            if (isLoadingMore) {
                return;
            }

            if (!isLoadingMore && (mTotalItemCount - mVisibleItemCount) <= 0) {
                l.onLoadMore(loadMoreView);
                isLoadingMore = true;
            }
        }

    }

    public interface OnLoadMoreListener {
        void onLoadMore(View loadMoreView);
    }
}
