package com.warlock.tmdb.ui.fragments.withoutDb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.warlock.tmdb.R
import com.warlock.tmdb.data.db.entity.MovieResult
import com.warlock.tmdb.databinding.ListItemRecomendedBinding
import com.warlock.tmdb.databinding.ListItemSearchResultBinding
import com.warlock.tmdb.ui.base.adapter.BaseListAdapter

class SearchAdapterBaseList(val list: ArrayList<MovieResult>, val listner: (item: MovieResult) -> Unit) :
    BaseListAdapter<MovieResult, ListItemSearchResultBinding>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<MovieResult>() {
        override fun areItemsTheSame(
            oldItem: MovieResult,
            newItem: MovieResult
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MovieResult,
            newItem: MovieResult
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun createBinding(parent: ViewGroup): ListItemSearchResultBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_search_result,
            parent,
            false
        )    }

    override fun bind(binding: ListItemSearchResultBinding, item: MovieResult?) {
        binding.movie = item
        binding.root.setOnClickListener {
            listner(item!!)
        }
    }
}