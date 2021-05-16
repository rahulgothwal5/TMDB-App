package com.warlock.tmdb.ui.fragments.withoutDb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.warlock.tmdb.R
import com.warlock.tmdb.data.db.entity.MovieResult
import com.warlock.tmdb.databinding.ListItemSearchResultBinding


class SearchAdapterPaged(val listner: (item: MovieResult) -> Unit) :
    PagedListAdapter<MovieResult, SearchAdapterPaged.MovieViewHolder>(DiffCallback()) {

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

    inner class MovieViewHolder(private val binding: ListItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(student: MovieResult) {
            binding.movie = student
            binding.root.setOnClickListener {
                listner(student)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_search_result,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item: MovieResult? = getItem(position)
        item?.let { holder.bind(it) }
    }

}