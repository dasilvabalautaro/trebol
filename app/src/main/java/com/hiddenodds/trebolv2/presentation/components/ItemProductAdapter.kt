package com.hiddenodds.trebolv2.presentation.components

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.presentation.model.MaterialModel
import com.hiddenodds.trebolv2.tools.DataListDiffCallback


class ItemProductAdapter(private val listener: (MaterialModel) -> Unit):
        RecyclerView.Adapter<ItemProductAdapter.ViewHolder>() {
    private val items: ArrayList<MaterialModel> = ArrayList()

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int)=
            holder!!.bind(items[position], listener)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int):
            ViewHolder{
        val view: ItemRowProductView = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.view_row_product,
                        parent, false) as ItemRowProductView
        return ViewHolder(view)

    }

    fun setObjectList(itemList: ArrayList<MaterialModel>){
        val diffResult: DiffUtil.DiffResult = DiffUtil
                .calculateDiff(DataListDiffCallback(this.items, itemList))
        this.items.clear()
        this.items.addAll(itemList)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(private val itemRowProductView: ItemRowProductView):
            RecyclerView.ViewHolder(itemRowProductView) {

        fun bind(item: MaterialModel, listener:
        (MaterialModel) -> Unit) = with(itemRowProductView) {
            tvCode!!.text = item.code
            tvDescription!!.text = item.detail
            setOnClickListener {
                itemView.background =  resources.getDrawable(R.color.colorPrimary)
                listener(item)
            }
        }
    }

}