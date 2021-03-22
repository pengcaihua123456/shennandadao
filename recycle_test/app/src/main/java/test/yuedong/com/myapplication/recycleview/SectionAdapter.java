package test.yuedong.com.myapplication.recycleview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by virl on 15/5/29.
 */
@Deprecated
public abstract class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.StaticListViewHolder> implements View.OnClickListener {

    @Override
    public StaticListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == kItemTypeSectionHeader) {
            return new StaticListViewHolder(headerView(parent));
        } else {
            View cellView = itemView(parent);
            cellView.setOnClickListener(this);
            return new StaticListViewHolder(cellView);
        }
    }

    @Override
    public void onBindViewHolder(StaticListViewHolder holder, int position) {
        ItemInfo itemInfo = items.get(position);
        if (itemInfo.itemIndex == kIndexHeader) {
            updateHeaderView(itemInfo.sectionIndex, holder.itemView);
        } else {
            updateItemView(itemInfo.sectionIndex, itemInfo.itemIndex, holder.itemView);
            holder.itemView.setTag(itemInfo);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).itemIndex == kIndexHeader ? kItemTypeSectionHeader : kItemTypeCell;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected abstract int getSectionCount();

    protected abstract int getItemCountForSection(int sectionIndex);

    protected abstract boolean hasSectionHeader();

    protected abstract View headerView(ViewGroup parent);

    protected abstract void updateHeaderView(int sectionIndex, View headerView);

    protected abstract View itemView(ViewGroup parent);

    protected abstract void updateItemView(int sectionIndex, int itemIndex, View itemView);

    protected abstract void onItemClicked(int sectionIndex, int itemIndex);

    protected static final int kItemTypeSectionHeader = 1;
    protected static final int kItemTypeCell = 2;

    public ItemDecoration itemDecoration(int color, int height, int leftMargin) {
        return new ItemDecoration(color, height, leftMargin);
    }

    public class ItemDecoration extends RecyclerView.ItemDecoration {
        private int height;
        private Drawable drawable;
        private int leftMargin;

        public ItemDecoration(int color, int height, int leftMargin) {
            this.height = height;
            drawable = new ColorDrawable(color);
            this.leftMargin = leftMargin;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            if (position == RecyclerView.NO_POSITION) {
                return;
            }
            ItemInfo item = items.get(position);
            if (item.itemIndex == kIndexHeader) {
                return;
            } else if (item.itemIndex + 1 == getItemCountForSection(item.sectionIndex)) {
                return;
            }
            outRect.set(0, 0, 0, height);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);
            final int left = parent.getPaddingLeft() + leftMargin;
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            int top = 1;
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int newTop = child.getTop() + params.topMargin;
                if (newTop > top) {
                    drawable.setBounds(left, newTop - height, right, newTop);
                    drawable.draw(c);
                }
                top = child.getBottom() + params.bottomMargin + 1;
            }
        }
    }

    @Override
    public void onClick(View v) {
        ItemInfo itemInfo = (ItemInfo) v.getTag();
        onItemClicked(itemInfo.sectionIndex, itemInfo.itemIndex);
    }

    private static class ItemInfo {
        int sectionIndex;
        int itemIndex;

        ItemInfo(int sectionIndex, int itemIndex) {
            this.sectionIndex = sectionIndex;
            this.itemIndex = itemIndex;
        }
    }

    private ArrayList<ItemInfo> items = new ArrayList<>();
    private static final int kIndexHeader = -100;

    public void notifySectionHeaderUpdate(int sectionIndex) {
        for (ItemInfo info : items) {
            if (info.sectionIndex == sectionIndex && info.itemIndex == kIndexHeader) {
                int index = items.indexOf(info);
                notifyItemChanged(index);
                return;
            }
        }
    }

    public void reloadData() {
        items.clear();
        boolean hasSectionHeader = hasSectionHeader();
        int sectionCount = getSectionCount();
        for (int index = 0; index != sectionCount; ++index) {
            int itemCount = getItemCountForSection(index);
            if (hasSectionHeader) {
                items.add(new ItemInfo(index, kIndexHeader));
            }
            for (int itemIndex = 0; itemIndex != itemCount; ++itemIndex) {
                items.add(new ItemInfo(index, itemIndex));
            }
        }
        notifyDataSetChanged();
    }

    public void notifyItemChanged(int sectionIndex, int itemIndex) {
        int size = items.size();
        for (int i = 0; i != size; ++i) {
            ItemInfo item = items.get(i);
            if (item.sectionIndex == sectionIndex && item.itemIndex == itemIndex) {
                notifyItemChanged(i);
                return;
            }
        }
    }

    public class StaticListViewHolder extends RecyclerView.ViewHolder {

        public StaticListViewHolder(View itemView) {
            super(itemView);
        }
    }
}
