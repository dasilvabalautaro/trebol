package com.hiddenodds.trebolv2.presentation.view.fragments

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import butterknife.BindView
import butterknife.ButterKnife
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.presentation.components.ItemProductAdapter
import com.hiddenodds.trebolv2.presentation.interfaces.ILoadDataView
import com.hiddenodds.trebolv2.presentation.model.MaterialModel
import com.hiddenodds.trebolv2.tools.ChangeFormat
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

class ProductFragment: NotificationFragment(), ILoadDataView {

    @BindView(R.id.rv_products)
    @JvmField var rvProducts: RecyclerView? = null
    @BindView(R.id.sv_product)
    @JvmField var svProduct: SearchView? = null

    companion object Factory {
        private const val inputTechnical = "technical_"
        fun newInstance(arg1: String? = null):
                ProductFragment = ProductFragment().apply{
            this.arguments = Bundle().apply {

                this.putString(inputTechnical, arg1)
            }

        }
    }

    private val codeTechnical: String by lazy { this.arguments.getString(inputTechnical) }

    private var adapter: ItemProductAdapter? = null
    private var listMaterial: ArrayList<MaterialModel>? = null
    private var inputMethodManager: InputMethodManager ? = null
    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root: View = inflater!!.inflate(R.layout.view_list_product,
                container,false)
        ButterKnife.bind(this, root)
        return root
    }

    override fun onResume() {
        super.onResume()

        ChangeFormat.deleteCacheTechnical(codeTechnical)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        materialPresenter.view = this
        setupRecyclerView()
        svProduct!!.queryHint = resources.getString(R.string.lbl_hint_search)
        async {
            materialPresenter.executeGetMaterial()
        }
        inputMethodManager = context
                .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onStart() {
        super.onStart()
        svProduct!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return if (newText.length > 2){
                    async(CommonPool) {
                        val queryList = listMaterial!!
                                .filter{ it.code.contains(newText.toUpperCase())} as ArrayList
                        if (queryList.isNotEmpty()){
                            adapter!!.setObjectList(queryList)
                            activity.runOnUiThread({
                                rvProducts!!.refreshDrawableState()
                                rvProducts!!.scrollToPosition(0)

                            })
                        }
                    }
                    true

                }else{
                    false
                }
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                inputMethodManager!!.toggleSoftInput(InputMethodManager
                        .HIDE_IMPLICIT_ONLY, 0)
                return true
            }

        })
    }

    private fun setupRecyclerView(){
        rvProducts!!.setHasFixedSize(true)
        rvProducts!!.layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ChangeFormat.addDecorationRecycler(rvProducts!!, context)
        }
        adapter = ItemProductAdapter{
            val materialModel = MaterialModel()
            materialModel.code = it.code
            materialModel.detail = it.detail
            materialModel.id = it.id
            listMaterialSelect.add(materialModel)

        }

        rvProducts!!.adapter = adapter
        rvProducts!!.adapter
    }


    override fun showMessage(message: String) {
        context.toast(message)
    }

    override fun showError(message: String) {
        context.toast(message)
    }

    override fun <T> executeTask(obj: T) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T> executeTask(objList: List<T>) {
        if (objList.isNotEmpty()){
            async {
                listMaterial = ArrayList(objList.filterIsInstance<MaterialModel>() as ArrayList)
                adapter!!.setObjectList(listMaterial!!)
                activity.runOnUiThread({
                    rvProducts!!.scrollToPosition(0)
                })

            }

        }else{
            context.toast(context.getString(R.string.list_not_found))
        }

    }


}