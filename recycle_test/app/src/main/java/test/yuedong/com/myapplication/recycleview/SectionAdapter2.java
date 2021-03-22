package test.yuedong.com.myapplication.recycleview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by virl on 15/7/5.
 */
@Deprecated
public abstract class SectionAdapter2 extends RecyclerView.Adapter<SectionAdapter2.MyViewHolder> {

    public SectionAdapter2() {
        reloadData();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(cellView(viewType, parent));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ItemInfo info = itemInfos.get(position);
        bindCellView(holder.itemView, info.sectionIndex, info.itemIndex);
    }

    @Override
    public int getItemCount() {
        return itemInfos.size();
    }

    @Override
    public int getItemViewType(int position) {
        ItemInfo info = itemInfos.get(position);
        return cellType(info.sectionIndex, info.itemIndex);
    }

    protected abstract int sectionCount();

    protected abstract int itemCountOfSection(int sectionIndex);

    protected abstract int cellType(int sectionIndex, int itemIndex);

    protected abstract View cellView(int cellType, ViewGroup parent);

    protected abstract void bindCellView(View view, int sectionIndex, int itemIndex);

    private ArrayList<ItemInfo> itemInfos = new ArrayList<>();

    public void reloadData() {
        itemInfos.clear();
        int sectionCount = sectionCount();
        for (int i = 0; i != sectionCount; ++i) {
            int itemCount = itemCountOfSection(i);
            for (int j = 0; j != itemCount; ++j) {
                itemInfos.add(new ItemInfo(i, j));
            }
        }
        notifyDataSetChanged();
    }

    public void notifySectionInsert(int sectionIndex, int itemIndex) {
        int position = 0;
        for(int i=0; i!=sectionIndex; ++i) {
            position += itemCountOfSection(i);
        }
        itemInfos.add(position + itemCountOfSection(sectionIndex) - 1,
                new ItemInfo(sectionIndex, itemCountOfSection(sectionIndex) - 1));
        position += itemIndex;
        notifyItemInserted(position);
    }

    public void notifySectionItemUpdate(int sectionIndex, int itemIndex) {
        int position = 0;
        for(int i=0; i!=sectionIndex; ++i) {
            position += itemCountOfSection(i);
        }
        position += itemIndex;
        notifyItemChanged(position);
    }

    public void notifySectionItemRemoved(int sectionIndex, int itemIndex) {
        int position = 0;
        for(int i=0; i!=sectionIndex; ++i) {
            position += itemCountOfSection(i);
        }
        itemInfos.remove(position + itemCountOfSection(sectionIndex));
        position += itemIndex;
        notifyItemRemoved(position);
    }

    public void notifySectionRangeInsert(int sectionIndex, int itemIndex, int count) {
        int position = 0;
        for(int i=0; i!=sectionIndex; ++i) {
            position += itemCountOfSection(i);
        }
        int start = position + itemIndex;
        int sectionLast = position + itemCountOfSection(sectionIndex) - count;
        for(int i=0; i!=count; ++i) {
            itemInfos.add(sectionLast+i, new ItemInfo(sectionIndex, i+itemIndex));
        }

        notifyItemRangeInserted(start, count);
    }

    public void notifySectionUpdate(int sectionIndex) {
        reloadData();
    }

//    public void notifySectionUpdate(int sectionIndex) {
//        if(itemInfos.isEmpty()) {
//            reloadData();
//            return;
//        }
//        int index = 0;
//        int count = 0;
//        int startIndex = 0;
//        int size = itemInfos.size();
//        for(index=0; index!=size; ++index) {
//            ItemInfo itemInfo = itemInfos.get(index);
//            if(itemInfo.sectionIndex == sectionIndex) {
//                startIndex = index;
//                count = 1;
//                while(itemInfo.sectionIndex == sectionIndex) {
//                    ++count;
//                    ++index;
//                    itemInfo = itemInfos.get(index);
//                }
//                break;
//            }
//        }
//    }

    private static class ItemInfo {
        int sectionIndex;
        int itemIndex;

        ItemInfo(int sectionIndex, int itemIndex) {
            this.sectionIndex = sectionIndex;
            this.itemIndex = itemIndex;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
