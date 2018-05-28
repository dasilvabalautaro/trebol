package com.hiddenodds.trebolv2.tools

import android.support.v7.util.DiffUtil

class DataListDiffCallback<T>(private val oldList: ArrayList<T>?,
                              private val newList: ArrayList<T>?):
        DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int,
                                 newItemPosition: Int): Boolean {
        return newList!![newItemPosition] == oldList!![oldItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldList?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return newList?.size ?: 0
    }

    override fun areContentsTheSame(oldItemPosition: Int,
                                    newItemPosition: Int): Boolean {
        return newList!![newItemPosition] == oldList!![oldItemPosition]
    }

}