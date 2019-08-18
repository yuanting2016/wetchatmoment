package com.easywetchat.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easywetchat.R;
import com.easywetchat.item.FooterItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * can be used in all kinds of recycleview style
 * easy and efficient
 */
public class MultiTypeAdapter extends RecyclerView.Adapter<MultiTypeAdapter.ItemViewHolder> {

    private final String DEFAULT_SIGN = new Random().nextLong() + "";

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    private LayoutInflater mInflater;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private OnViewChange onViewChange;

    public MultiTypeAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnViewChange(OnViewChange onViewChange) {
        this.onViewChange = onViewChange;
    }

    public interface IItem {
        /**
         * @return layout
         */
        int getLayout();

        /**
         * @return VariableId
         */
        int getVariableId();

        void attachAdapter(MultiTypeAdapter adapter);

        void onBinding(ViewDataBinding binding);

        void onBinding(ViewDataBinding binding, int position, Object payload);

        void unBinding();

        long getItemId(int position);

        void onViewAttachedToWindow(ItemViewHolder holder);

        void onViewDetachedFromWindow(ItemViewHolder holder);

        boolean doOnScreenShot();

        IItem clone();
    }

    public interface OnViewChange {
        void onViewAttachedToWindow(ItemViewHolder holder);

        void onViewDetachedFromWindow(ItemViewHolder holder);
    }

    private List<IItem> items = new ArrayList<>();

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        if (!hasStableIds()) {
            return RecyclerView.NO_ID;
        }

        if (items.isEmpty() || position >= items.size()) {
            return new Random().nextLong();
        }

        return items.get(position).getItemId(position);
    }


    @Override
    public int getItemViewType(int position) {
        return items.get(position).getLayout();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(mInflater, viewType, parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        if (holder.getAdapterPosition() < 0) { // <0 : item has not been refreshed or deleted
            holder.itemView.setOnClickListener(null);
            holder.itemView.setOnLongClickListener(null);
            return;
        }

        setItemOnClick(holder, hasTag(holder.itemView, R.id.item_click) ? null : new ClickByPosition(holder.getAdapterPosition()) {
            @Override
            void onClickByPos(View v, int pos) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(pos);
                }
            }
        });

        setItemOnLongClick(holder, hasTag(holder.itemView, R.id.item_long_press) ? null : new LongClickByPosition(holder.getAdapterPosition()) {
            @Override
            boolean onLongClickByPos(View v, int pos) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(pos);
                }
                return true;
            }
        });
        IItem iItem = items.get(holder.getAdapterPosition());
        iItem.attachAdapter(this);
        iItem.onBinding(holder.getBinding());
        holder.bindTo(items.get(holder.getAdapterPosition()));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position, List<Object> payloads) {
        if (payloads == null || payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
            return;
        }

        IItem iItem = items.get(holder.getAdapterPosition());
        iItem.attachAdapter(this);
        iItem.onBinding(holder.getBinding(), holder.getAdapterPosition(), payloads.get(0));
    }


    /**
     * check view has set tag
     * @param view
     * @param tag
     * @return
     */
    private boolean hasTag(View view, @IdRes int tag) {
        Object obj = view.getTag(tag);
        return obj != null;
    }

    //
    private void setItemOnClick(ItemViewHolder holder, ClickByPosition clickListener) {
        Object obj = holder.itemView.getTag(R.id.item_click);
        if (obj == null) {
            holder.itemView.setTag(R.id.item_click, clickListener);
        } else {
            ClickByPosition click = (ClickByPosition) obj;
            click.pos = holder.getAdapterPosition();
            clickListener = click;
        }
        holder.itemView.setOnClickListener(clickListener);
    }

    private void setItemOnLongClick(ItemViewHolder holder, LongClickByPosition clickListener) {
        Object obj = holder.itemView.getTag(R.id.item_long_press);
        if (obj == null) {
            holder.itemView.setTag(R.id.item_long_press, clickListener);
        } else {
            LongClickByPosition click = (LongClickByPosition) obj;
            click.pos = holder.getAdapterPosition();
            clickListener = click;
        }
        holder.itemView.setOnLongClickListener(clickListener);
    }

    @Override
    public void onViewAttachedToWindow(ItemViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int pos = holder.getAdapterPosition();
        if (items != null && items.size() > pos && pos >= 0) {
            IItem iItem = items.get(holder.getAdapterPosition());
            iItem.attachAdapter(this);
            iItem.onViewAttachedToWindow(holder);
        }
        if (onViewChange != null) {
            onViewChange.onViewAttachedToWindow(holder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(ItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        int pos = holder.getAdapterPosition();
        if (items != null && items.size() > pos && pos >= 0) {
            items.get(holder.getAdapterPosition()).onViewDetachedFromWindow(holder);
        }
        if (holder.getItem() != null) {
            holder.getItem().unBinding();
        }
        if (onViewChange != null) {
            onViewChange.onViewDetachedFromWindow(holder);
        }
    }

    private static abstract class ClickByPosition implements View.OnClickListener {

        private int pos;

        public ClickByPosition(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            onClickByPos(v, pos);
        }

        abstract void onClickByPos(View v, int pos);
    }

    private static abstract class LongClickByPosition implements View.OnLongClickListener {

        private int pos;

        public LongClickByPosition(int pos) {
            this.pos = pos;
        }


        @Override
        public boolean onLongClick(View v) {
            return onLongClickByPos(v, pos);
        }

        abstract boolean onLongClickByPos(View v, int pos);
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;
        private IItem iItem;

        public ItemViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void bindTo(IItem item) {
            this.iItem = item;
            binding.setVariable(item.getVariableId(), item);
            binding.executePendingBindings();
        }

        public ViewDataBinding getBinding() {
            return binding;
        }

        public IItem getItem() {
            return iItem;
        }
    }

    public void setItem(IItem item) {
        clearItems();
        addItem(item);
    }

    public void setItems(List<IItem> items) {
        clearItems();
        addItems(items);
    }

    public List<IItem> getItems() {
        return items;
    }

    public IItem getItem(int position) {
        return items.get(position);
    }

    public void addItem(IItem item) {
        if (item instanceof FooterItem) {
            Iterator<IItem> it = items.iterator();
            while (it.hasNext()) {
                IItem next = it.next();
                if (next instanceof FooterItem) {
                    it.remove();
                }
            }
        }
        items.add(item);
    }


    public void addItem(IItem item, int index) {
        items.add(index, item);
    }

    public void setItem(IItem item, int index) {
        items.set(index, item);
    }

    public void addItems(List<IItem> items) {
        this.items.addAll(items);
    }

    public void addItems(Collection<? extends IItem> items) {
        this.items.addAll(items);
    }

    public int removeItem(IItem item) {
        int pos = findPos(item);
        items.remove(item);
        return pos;
    }

    public void removeItem(int index) {
        items.remove(index);
    }

    public void clearItems() {
        items.clear();
    }

    public int findPos(IItem item) {
        return items.indexOf(item);
    }


    public String getSignature() {
        return DEFAULT_SIGN;
    }

}