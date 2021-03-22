package test.yuedong.com.myapplication.recycleview;

import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by virl on 16/3/10.
 */
public abstract class RecyclerViewSectionAdapter<T extends RecyclerViewSectionAdapter.SectionViewHolder> extends RecyclerView.Adapter<T> {

    @Override
    public void onBindViewHolder(T holder, int position) {
        ItemInfo itemInfo = itemInfos.get(position);
        if(itemInfo.itemIndex == -1) {
            bindHeaderViewHolder(holder, itemInfo.sectionIndex);
        } else {
            bindItemViewHolder(holder, itemInfo.sectionIndex, itemInfo.itemIndex);
        }
    }

    @Override
    public final int getItemCount() {
        return itemInfos.size();
    }

    protected abstract int sectionCount();
    protected abstract int itemCountOfSection(int section);
    protected abstract int viewType(int section, int itemIndex);
    protected abstract int headerViewType(int section);
    protected abstract boolean hasHeader(int section);
    protected abstract void bindHeaderViewHolder(T holder, int section);
    protected abstract void bindItemViewHolder(T holder, int section, int itemIndex);

    public static class SectionViewHolder extends ViewHolder {
        public SectionViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        ItemInfo itemInfo = itemInfos.get(position);
        if(itemInfo.itemIndex == -1) {
            return headerViewType(itemInfo.sectionIndex);
        }
        return viewType(itemInfo.sectionIndex, itemInfo.itemIndex);
    }

    public int getSectionIndexAtPosition(int position) {
        ItemInfo itemInfo = itemInfos.get(position);
        return itemInfo.sectionIndex;
    }

    public int getPositionOfSection(int sectionIndex) {
        int index = 0;
        for(ItemInfo itemInfo : itemInfos) {
            if(itemInfo.sectionIndex == sectionIndex) {
                return index;
            }
            ++index;
        }
        return 0;
    }

    public void reloadData() {
        itemInfos.clear();
        int sectionCount = sectionCount();
        for (int i = 0; i != sectionCount; ++i) {
            if(hasHeader(i)) {
                itemInfos.add(new ItemInfo(i, -1));
            }
            int itemCount = itemCountOfSection(i);
            for (int j = 0; j != itemCount; ++j) {
                itemInfos.add(new ItemInfo(i, j));
            }
        }
        notifyDataSetChanged();
    }

    private ArrayList<ItemInfo> itemInfos = new ArrayList<>();

    private static class ItemInfo {
        int sectionIndex;
        int itemIndex;

        ItemInfo(int sectionIndex, int itemIndex) {
            this.sectionIndex = sectionIndex;
            this.itemIndex = itemIndex;
        }
    }

    public RecyclerView.ItemDecoration buildItemDecoration(int dividerSize, int marginLeft, int marginRight, int color) {
        return new SectionItemDecoration(dividerSize, marginLeft, marginRight, color);
    }

    private class SectionItemDecoration extends RecyclerView.ItemDecoration {
        int dividerSize;
        int marginLeft;
        int marginRight;
        private Drawable drawable;
        public SectionItemDecoration(int dividerSize, int marginLeft, int marginRight, int color) {
            this.dividerSize = dividerSize;
            this.marginLeft = marginLeft;
            this.marginRight = marginRight;
            drawable = new ColorDrawable(color);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);
            final int left = parent.getPaddingLeft() + marginLeft;
            final int right = parent.getWidth() - marginRight;

            final int childCount = parent.getChildCount();
            int lastIndex = itemInfos.size() - 1;
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                int position = parent.getChildAdapterPosition(child);
                if(position == lastIndex) {
                    continue;
                }
                ItemInfo itemInfo = itemInfos.get(position);
                if(itemInfo.itemIndex >= 0) {
                    final int bottom = child.getBottom();
                    drawable.setBounds(left, bottom - dividerSize, right, bottom);
                    drawable.draw(c);
                }
            }
        }
    }
}
