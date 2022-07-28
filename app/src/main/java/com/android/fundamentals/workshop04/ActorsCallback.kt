package com.android.fundamentals.workshop04

import androidx.recyclerview.widget.DiffUtil
import com.android.fundamentals.data.models.Actor

class ActorsCallback(
    private val oldList: List<Actor>,
    private val newList: List<Actor>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].name == newList[newItemPosition].name

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}