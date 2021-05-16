package com.warlock.tmdb.ui.base.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

interface AdapterBindingHelper<T, V : ViewDataBinding> {

    /**
     * to create viewBinding for particular view type
     * @param parent ViewGroup
     * @return V viewBinding
     */
    fun createBinding(parent: ViewGroup): V

    /**
     * Called when you have to bind view with item
     * @param binding viewBinding of view
     * @param item item to be bind with viewBinding
     */
    fun bind(binding: V, item: T?)
}