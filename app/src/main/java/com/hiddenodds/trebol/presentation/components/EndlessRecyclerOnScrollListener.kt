package com.hiddenodds.trebol.presentation.components

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerOnScrollListener: RecyclerView.OnScrollListener() {
    private var previousVisibleItem = 0
    private var flag = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = recyclerView.childCount
        val totalItemCount = recyclerView.layoutManager!!.itemCount
        val firstVisibleItem = (recyclerView
                .layoutManager as LinearLayoutManager)
                .findFirstVisibleItemPosition()
        if (firstVisibleItem == 0){
            previousVisibleItem = 0
        }

        if (firstVisibleItem > previousVisibleItem){
            previousVisibleItem = firstVisibleItem
            flag = true
        }else{
            flag = false
        }

        if (flag && ((totalItemCount - visibleItemCount) <=
                        (firstVisibleItem + visibleItemCount))) {
            onLoadMore()
        }
    }

    abstract fun onLoadMore()
}