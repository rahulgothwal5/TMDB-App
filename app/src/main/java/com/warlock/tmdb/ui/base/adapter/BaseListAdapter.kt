package com.warlock.tmdb.ui.base.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.warlock.tmdb.ui.base.BaseViewHolder

abstract class BaseListAdapter<T, V : ViewDataBinding>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, BaseViewHolder<V>>(
    AsyncDifferConfig.Builder<T>(diffCallback)
        .build()
), AdapterBindingHelper<T, V> {

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

    /**
     * to create viewBinding for particular view type
     * @param parent ViewGroup
     * @param viewType
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
}
