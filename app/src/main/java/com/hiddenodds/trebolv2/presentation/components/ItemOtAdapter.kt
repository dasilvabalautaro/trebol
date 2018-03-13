package com.hiddenodds.trebolv2.presentation.components

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.presentation.model.NotificationModel
import com.hiddenodds.trebolv2.tools.DataListDiffCallback


class ItemOtAdapter: RecyclerView.Adapter<ItemOtAdapter.ViewHolder>() {
    private val items: ArrayList<NotificationModel> = ArrayList()

    override fun onBindViewHolder(holder: ItemOtAdapter.ViewHolder?, position: Int) =
            holder!!.bind(items[position])

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup?,
                                    viewType: Int): ViewHolder {
        val view: ItemRowOtView = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.view_row_ot,
                        parent, false) as ItemRowOtView
        return ViewHolder(view)
    }

    fun setObjectList(itemList: ArrayList<NotificationModel>){
        val diffResult: DiffUtil.DiffResult = DiffUtil
                .calculateDiff(DataListDiffCallback(this.items, itemList))
        this.items.clear()
        this.items.addAll(itemList)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(private val itemRowOtView: ItemRowOtView):
            RecyclerView.ViewHolder(itemRowOtView){
        fun bind(item: NotificationModel) = with(itemRowOtView)  {
            edtAnnounce!!.text = item.code
            edtMachine!!.text = item.series
            edtType!!.text = item.type
            edtName!!.text = item.businessName
            edtDate!!.text = item.dateInit
            edtDateFinish!!.text = item.dateCompleted
            edtProvince!!.text = item.province
            edtLocation!!.text = item.locality
            edtObservation!!.text = item.symptom
            edtAddress!!.text = item.address
        }
    }
}