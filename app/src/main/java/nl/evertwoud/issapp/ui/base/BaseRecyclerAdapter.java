package nl.evertwoud.issapp.ui.base;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


/**
 * The type Base recycler view adapter.
 *
 * @param <T> the type parameter
 * @param <V> the type parameter
 */
public abstract class BaseRecyclerAdapter<T, V extends View> extends RecyclerView.Adapter<ViewWrapper<V>> {

    protected final List<T> items = new ArrayList<>();
    private RecyclerItemClickListener<T> mRecyclerItemClickListener;

    @Override
    public final ViewWrapper<V> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper<>(onCreateItemView(parent, viewType));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Sets items.
     *
     * @param items  the items
     * @param notify the notify
     */
    public void setItems(List<T> items, boolean notify) {
        this.items.clear();
        this.items.addAll(items);
        if (notify) {
            notifyDataSetChanged();
        }
    }

    /**
     * Sets items.
     *
     * @param items the items
     */
    public void setItems(List<T> items) {
        setItems(items, true);
    }

    /**
     * Add item.
     *
     * @param item the item
     */
    public void addItem(T item) {
        items.add(item);
        notifyItemInserted(items.size());
    }

    /**
     * Add item.
     *
     * @param index the index
     * @param item  the item
     */
    public void addItem(int index, T item) {
        items.add(index, item);
        notifyItemInserted(index);
    }

    /**
     * Remove item.
     *
     * @param pos the pos
     */
    public void removeItem(int pos) {
        items.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, getItemCount() - pos);
    }

    /**
     * Remove item.
     *
     * @param pItem the p item
     */
    public void removeItem(T pItem) {
        final int index = items.indexOf(pItem);
        if (index != -1) {
            removeItem(index);
        }
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public List<T> getData() {
        return items;
    }

    /**
     * Gets item.
     *
     * @param position the position
     * @return the item
     */
    public T getItem(int position) {
        if (position > -1 && position < items.size()) {
            return items.get(position);
        }
        return null;
    }

    /**
     * Index of int.
     *
     * @param pItem the p item
     * @return the int
     */
    public int indexOf(T pItem) {
        return items.indexOf(pItem);
    }

    /**
     * On create item view v.
     *
     * @param parent   the parent
     * @param viewType the view type
     * @return the v
     */
    protected abstract V onCreateItemView(ViewGroup parent, int viewType);

    /**
     * Set item click listner.
     *
     * @param clickListener the click listener
     */
    public void setItemClickListner(RecyclerItemClickListener<T> clickListener) {
        mRecyclerItemClickListener = clickListener;
    }

    /**
     * Get recycler item click listener recycler item click listener.
     *
     * @return the recycler item click listener
     */
    public RecyclerItemClickListener<T> getRecyclerItemClickListener() {
        return mRecyclerItemClickListener;
    }

    /**
     * Recycler Click Listener interface
     *
     * @param <Model> model
     */
    public interface RecyclerItemClickListener<Model> {
        /**
         * On item click
         *
         * @param item clicked item
         */
        void onItemClick(Model item);
    }

}