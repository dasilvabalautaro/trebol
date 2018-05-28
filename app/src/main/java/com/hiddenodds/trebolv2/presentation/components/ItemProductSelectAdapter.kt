package com.hiddenodds.trebolv2.presentation.components

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.presentation.model.AssignedMaterialModel
import com.hiddenodds.trebolv2.tools.DataListDiffCallback

class ItemProductSelectAdapter(private val listener:
                               (AssignedMaterialModel) -> Unit):
        RecyclerView.Adapter<ItemProductSelectAdapter.ViewHolder>(){
    private val items: ArrayList<AssignedMaterialModel> = ArrayList()

    override fun onBindViewHolder(holder: ItemProductSelectAdapter.ViewHolder?,
                                  position: Int) =
            holder!!.bind(items[position], listener)

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup?,
                                    viewType: Int):
            ItemProductSelectAdapter.ViewHolder {
        val view: ItemRowProductSelectView = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.view_row_product_select,
                        parent, false) as ItemRowProductSelectView
        return ItemProductSelectAdapter.ViewHolder(view)
    }

    fun setObjectList(itemList: ArrayList<AssignedMaterialModel>){
        val diffResult: DiffUtil.DiffResult = DiffUtil
                .calculateDiff(DataListDiffCallback(this.items, itemList))
        this.items.clear()
        this.items.addAll(itemList)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(private val itemRowProductSelectView: ItemRowProductSelectView):
            RecyclerView.ViewHolder(itemRowProductSelectView){
        fun bind(item: AssignedMaterialModel, listener:
        (AssignedMaterialModel) -> Unit) = with(itemRowProductSelectView){
            etQuantity!!.tag = item.id
            etQuantity!!.setText(item.quantity.toString())
            if (item.material != null){
                tvCode!!.text = item.material!!.code
                tvDetail!!.text = item.material!!.detail
            }

            if (item.change == 3){
                ibDelete!!.visibility = View.INVISIBLE
            }else{
                ibDelete!!.visibility = View.VISIBLE
            }

            ibDelete!!.setOnClickListener {
                listener(item)
            }

            etQuantity!!.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s != null && s.isNotEmpty()){
                        item.quantity = s.toString().toInt()
                        if (item.id.trim().isNotEmpty()){
                            item.change = 1
                            listener(item)
                        }

                    }

                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })
        }
    }
}