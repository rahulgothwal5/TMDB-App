package com.warlock.tmdb.ui.fragments.infoScreen.adapters

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.warlock.tmdb.R
import com.warlock.tmdb.data.db.entity.Cast
import com.warlock.tmdb.databinding.ListItemCastBinding

class CastAdapter(
    val list: ArrayList<Cast>,
    val listner: (item: Cast) -> Unit
) :
    ListAdapter<Cast, CastAdapter.CastViewHolder>(DiffCallback()), Parcelable {

    class DiffCallback : DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(
            oldItem: Cast,
            newItem: Cast
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Cast,
            newItem: Cast
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    inner class CastViewHolder(private val binding: ListItemCastBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(cast: Cast) {
            binding.cast = cast
            binding.root.setOnClickListener {
                listner(cast)
            }
        }
    }

    constructor(parcel: Parcel) : this(
        TODO("list"),
        TODO("listner")
    ) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_cast,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CastAdapter> {
        override fun createFromParcel(parcel: Parcel): CastAdapter {
            return CastAdapter(parcel)
        }

        override fun newArray(size: Int): Array<CastAdapter?> {
            return arrayOfNulls(size)
        }
    }
}