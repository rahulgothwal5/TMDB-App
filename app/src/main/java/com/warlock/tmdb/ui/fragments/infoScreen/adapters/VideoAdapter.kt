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
import com.warlock.tmdb.data.db.entity.RelatedVideos
import com.warlock.tmdb.databinding.ListItemVideoBinding

class VideoAdapter(
    val list: ArrayList<RelatedVideos>,
    val listner: (item: RelatedVideos) -> Unit
) :
    ListAdapter<RelatedVideos, VideoAdapter.CastViewHolder>(DiffCallback()), Parcelable {

    class DiffCallback : DiffUtil.ItemCallback<RelatedVideos>() {
        override fun areItemsTheSame(
            oldItem: RelatedVideos,
            newItem: RelatedVideos
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RelatedVideos,
            newItem: RelatedVideos
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    inner class CastViewHolder(private val binding: ListItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(video: RelatedVideos) {
            binding.video = video
            binding.root.setOnClickListener {
                listner(video)
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
                R.layout.list_item_video,
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

    companion object CREATOR : Parcelable.Creator<VideoAdapter> {
        override fun createFromParcel(parcel: Parcel): VideoAdapter {
            return VideoAdapter(parcel)
        }

        override fun newArray(size: Int): Array<VideoAdapter?> {
            return arrayOfNulls(size)
        }
    }
}