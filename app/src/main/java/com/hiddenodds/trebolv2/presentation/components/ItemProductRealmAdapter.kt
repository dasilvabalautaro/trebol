package com.hiddenodds.trebolv2.presentation.components

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.model.data.Material
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter


class ItemProductRealmAdapter(data: OrderedRealmCollection<Material>?,
                              autoUpdate: Boolean, private val listener: (Material) -> Unit):
        RealmRecyclerViewAdapter<Material,
                ItemProductRealmAdapter.ViewHolder>(data, autoUpdate) {


    override fun onBindViewHolder(holder: ItemProductRealmAdapter.ViewHolder?, position: Int)=
            holder!!.bind(getItem(position)!!, listener)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int):
            ItemProductRealmAdapter.ViewHolder {
        val view: ItemRowProductView = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.view_row_product,
                        parent, false) as ItemRowProductView
        return ItemProductRealmAdapter.ViewHolder(view)

    }

    class ViewHolder(private val itemRowProductView: ItemRowProductView):
            RecyclerView.ViewHolder(itemRowProductView) {

        fun bind(item: Material, listener:
        (Material) -> Unit) = with(itemRowProductView) {
            tvCode!!.text = item.code
            tvDescription!!.text = item.detail
            setOnClickListener { listener(item) }
        }
    }
}