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
import com.warlock.tmdb.data.db.entity.Crew
import com.warlock.tmdb.databinding.ListItemCrewBinding

class CrewAdapter(
    val list: ArrayList<Crew>,
    val listner: (item: Crew) -> Unit
) :
    ListAdapter<Crew, CrewAdapter.CrewViewHolder>(DiffCallback()), Parcelable {

    class DiffCallback : DiffUtil.ItemCallback<Crew>() {
        override fun areItemsTheSame(
            oldItem: Crew,
            newItem: Crew
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Crew,
            newItem: Crew
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    inner class CrewViewHolder(private val binding: ListItemCrewBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(cast: Crew) {
            binding.crew = cast
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrewViewHolder {
        return CrewViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_crew,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CrewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CrewAdapter> {
        override fun createFromParcel(parcel: Parcel): CrewAdapter {
            return CrewAdapter(parcel)
        }

        override fun newArray(size: Int): Array<CrewAdapter?> {
            return arrayOfNulls(size)
        }
    }
}