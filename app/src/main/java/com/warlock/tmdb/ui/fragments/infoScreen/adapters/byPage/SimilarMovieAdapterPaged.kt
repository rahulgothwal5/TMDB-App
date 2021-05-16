package com.warlock.tmdb.ui.fragments.infoScreen.adapters.byPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.warlock.tmdb.R
import com.warlock.tmdb.data.db.entity.MovieResult
import com.warlock.tmdb.databinding.ListItemRecomendedBinding


class SimilarMovieAdapterPaged(val listner: (item: MovieResult) -> Unit) :
    PagedListAdapter<MovieResult, SimilarMovieAdapterPaged.MovieViewHolder>(DiffCallback()) {

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

    inner class MovieViewHolder(private val binding: ListItemRecomendedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieResult) {
            binding.result = movie
            binding.root.setOnClickListener {
                listner(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_recomended,
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