package com.warlock.tmdb.ui.base.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.warlock.tmdb.ui.base.BaseViewHolder


abstract class BaseRecyclerViewAdapter<T, V : ViewDataBinding> :
    RecyclerView.Adapter<BaseViewHolder<V>>(), AdapterBindingHelper<T, V> {
    /**
     * list of things for current adapter
     */
    private var currentList = ArrayList<T>()

    /**
     * Call when you want to update this adapter item have to pass list of things every time. diffResult dispatches the update events to the given adapter.
     * @param list List<T>
     */
    fun submitList(list: List<T>) {
        val diffResult = DiffUtil.calculateDiff(DiffCallback(currentList, list), true)
        diffResult.dispatchUpdatesTo(this)
        currentList.clear()
        currentList.addAll(list)
    }

    /**
     * use to get item of current list
     * @param position of item which want to access
     * @return T item for particular position
     */
    open fun getItem(position: Int) = currentList[position]

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent
     * an item.
     * @param parent ViewGroupThe ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     * @return BaseViewHolder<V>
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<V> {
        val binding = createBinding(parent)
        return BaseViewHolder(binding)
    }

    override fun getItemCount() = currentList.size

    /**
     * to create viewBinding for particular view type
     * @param parent ViewGroup
     * @return V viewBinding
     */
    abstract override fun createBinding(parent: ViewGroup): V

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * @param holder BaseViewHolder<V>
     * @param position Int
     */
    override fun onBindViewHolder(holder: BaseViewHolder<V>, position: Int) {
        bind(holder.binding, getItem(position))
        holder.binding.executePendingBindings()
    }

    /**
     * Called when you have to bind view with item
     * @param binding viewBinding of view
     * @param item item to be bind with viewBinding
     */
    abstract override fun bind(binding: V, item: T?)

    /**
     * Called by the DiffUtil to decide whether two object represent the same Item.
     * <p>
     * For example, if your items have unique ids, this method should check their id equality.
     *
     * @param oldItem item in the old list
     * @param newItem item in the new list
     * @return True if the two items represent the same object or false if they are different.
     */
    protected abstract fun areItemsTheSame(oldItem: T, newItem: T): Boolean

    /**
     * Called by the DiffUtil when it wants to check whether two items have the same data.
     * DiffUtil uses this information to detect if the contents of an item has changed.
     * <p>
     * DiffUtil uses this method to check equality instead of {@link Object#equals(Object)}
     * so that you can change its behavior depending on your UI.
     * For example, if you are using DiffUtil with a
     * {@link RecyclerView.Adapter RecyclerView.Adapter}, you should
     * return whether the items' visual representations are the same.
     * <p>
     * This method is called only if {@link #areItemsTheSame(int, int)} returns
     * {@code true} for these items.
     *
     * @param oldItem item in the old list
     * @param newItem item in the new list
     * @return True if the contents of the items are the same or false if they are different.
     */
    protected abstract fun areContentsTheSame(oldItem: T, newItem: T): Boolean

    /**
     * A Callback class used by DiffUtil while calculating the diff between two lists.
     * @property oldList List<T>
     * @property newList List<T>
     */
    inner class DiffCallback(private val oldList: List<T>, private val newList: List<T>) :
        DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            areItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            areContentsTheSame(oldList[oldItemPosition], newList[newItemPosition])

    }
}
