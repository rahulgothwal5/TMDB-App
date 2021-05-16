package com.warlock.tmdb.ui.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import com.warlock.tmdb.R
import com.warlock.tmdb.databinding.NetworkStateItemBinding
import com.warlock.tmdb.ui.base.BaseViewHolder
import com.warlock.tmdb.util.network.NetworkState
import com.warlock.tmdb.util.network.Status


abstract class BasePagedListAdapter<T, V : ViewDataBinding>(
    diffCallback: DiffUtil.ItemCallback<T>,
    private val addLoadingLayout: Boolean = false
) : PagedListAdapter<T, BaseViewHolder<V>>(
    AsyncDifferConfig.Builder<T>(diffCallback)
        .build()
), AdapterBindingHelper<T, V> {
    var retry: (() -> Any)? = null
    private var networkState: NetworkState? = null

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent
     * an item.
     * @param parent ViewGroupThe ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     * @return BaseViewHolder<V>
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<V> {
        return if (addLoadingLayout && viewType == R.layout.network_state_item) {
            BaseViewHolder(addLoadingLayout(parent))
        } else
            BaseViewHolder(createBinding(parent))
    }


    /**
     * Use to add loading view at runtime
     */
    private fun addLoadingLayout(parent: ViewGroup): V {
        val binding = createBinding(parent)
        val viewGroup = binding.root as ViewGroup
        val networkStateItemBinding =
            NetworkStateItemBinding.inflate(LayoutInflater.from(parent.context))
        viewGroup.removeAllViewsInLayout()
        viewGroup.addView(
            networkStateItemBinding.root,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return binding
    }

    /**
     * Called when you have to bind view with item
     * @param binding viewBinding of view
     * @param item item to be bind with viewBinding
     */
    abstract override fun bind(binding: V, item: T?)

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
        if (getItemViewType(position) == R.layout.network_state_item) {
            bindLoadingView((holder), networkState)
        } else {
            bind(holder.binding, getItem(position))
            holder.binding.executePendingBindings()
        }
    }

    /**
     * use to bind loading view on base of network state
     */
    open fun bindLoadingView(
        holder: BaseViewHolder<V>,
        networkState: NetworkState?
    ) {
        val progressBar: ProgressBar = holder.binding.root.findViewById(R.id.progress_bar)
        val retryButton: Button = holder.binding.root.findViewById(R.id.retry_button)
        val errorMessage: TextView = holder.binding.root.findViewById(R.id.error_msg)

        networkState?.status?.let {
            when (it) {
                Status.RUNNING -> {
                    progressBar.visibility = View.VISIBLE
                    retryButton.visibility = View.GONE
                    errorMessage.visibility = View.GONE
                }
                Status.FAILED -> {
                    progressBar.visibility = View.GONE
                    retryButton.visibility = View.VISIBLE
                    errorMessage.visibility = View.VISIBLE
                    errorMessage.text = networkState.msg
                    retryButton.setOnClickListener {
                        retry?.invoke()
                    }
                }
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    retryButton.visibility = View.GONE
                    errorMessage.visibility = View.GONE
                }
            }
        }

    }


    private fun hasExtraRow() =
        addLoadingLayout && networkState != null && networkState != NetworkState.LOADED

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1 && addLoadingLayout) {
            R.layout.network_state_item
        } else {
            1
        }
    }

}
