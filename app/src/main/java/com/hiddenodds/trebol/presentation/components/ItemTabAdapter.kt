package com.hiddenodds.trebol.presentation.components

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.presentation.model.GuideModel
import com.hiddenodds.trebol.tools.DataListDiffCallback
import java.util.*


class ItemTabAdapter(private val listener: (GuideModel) -> Unit):
        RecyclerView.Adapter<ItemTabAdapter.ViewHolder>(){
    private val items: ArrayList<GuideModel> = ArrayList()
    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: ItemTabAdapter.ViewHolder, position: Int)=
            holder.bind(items[position], listener)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ItemTabAdapter.ViewHolder {
        val view: ItemRowTabView = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_row_tab,
                        parent, false) as ItemRowTabView
        return ItemTabAdapter.ViewHolder(view)

    }

    fun setObjectList(itemList: ArrayList<GuideModel>){
        val diffResult: DiffUtil.DiffResult = DiffUtil
                .calculateDiff(DataListDiffCallback(this.items, itemList))
        this.items.clear()
        this.items.addAll(itemList)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(private val itemRowTabView: ItemRowTabView):
            RecyclerView.ViewHolder(itemRowTabView) {

        fun bind(item: GuideModel, listener:
        (GuideModel) -> Unit) = with(itemRowTabView) {
            etValue!!.setText(item.value)
            tvDescription!!.text = item.description
            if (item.free == 1){
                if (item.nameField != "nextHours"){
                    etValue!!.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
                }else{
                    etValue!!.inputType = InputType.TYPE_CLASS_NUMBER
                }

            }else{
                etValue!!.inputType = InputType.TYPE_NULL
            }

            etValue!!.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    if (item.free == 1){
                        listener(item)
                    }

                }
            }

            etValue!!.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s != null){
                        item.value = s.toString()
                       if (item.free != 1){
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