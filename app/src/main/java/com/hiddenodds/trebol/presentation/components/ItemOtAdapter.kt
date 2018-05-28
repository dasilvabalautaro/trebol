package com.hiddenodds.trebol.presentation.components

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.presentation.model.NotificationModel
import com.hiddenodds.trebol.tools.DataListDiffCallback


class ItemOtAdapter(private val listener:
                    (NotificationModel) -> Unit): RecyclerView.Adapter<ItemOtAdapter.ViewHolder>() {
    private val items: ArrayList<NotificationModel> = ArrayList()

    override fun onBindViewHolder(holder: ItemOtAdapter.ViewHolder?, position: Int) =
            holder!!.bind(items[position], listener)

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
        private val DEMO = "Demo"
        private val INSTALATION = "Instalacion"
        private val CM = "C.M."

        fun bind(item: NotificationModel?, listener:
        (NotificationModel) -> Unit) = with(itemRowOtView)  {
            if (item != null){
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
                if (item.customer != null){
                    edtPhone!!.text = item.customer!!.phone
                    edtContact!!.text = item.customer!!.name

                }
                if (item.type != DEMO && item.type != INSTALATION){
                    btnEmail!!.visibility = View.INVISIBLE
                }else{
                    btnEmail!!.visibility = View.VISIBLE
                }

                if (item.type == CM){
                    btnGuide!!.visibility = View.VISIBLE
                }else{
                    btnGuide!!.visibility = View.INVISIBLE
                }

                btnEmail!!.setOnClickListener {
                    listener(item)
                }

                btnOpenOt!!.tag = item.code + "*" + item.idTech
                btnGuide!!.tag = item.code + "*" + item.idTech
            }

        }
    }
}