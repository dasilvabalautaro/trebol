package com.hiddenodds.trebolv2.presentation.view.fragments

import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.model.data.Material
import com.hiddenodds.trebolv2.presentation.components.ItemProductRealmAdapter
import com.hiddenodds.trebolv2.presentation.interfaces.ILoadDataView
import com.hiddenodds.trebolv2.presentation.model.MaterialModel
import com.hiddenodds.trebolv2.tools.ChangeFormat
import io.realm.OrderedRealmCollection
import io.realm.RealmResults
import kotlinx.coroutines.experimental.async

class ProductFragment: NotificationFragment(), ILoadDataView {
    @BindView(R.id.rv_products)
    @JvmField var rvProducts: RecyclerView? = null

    private var adapter: ItemProductRealmAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root: View = inflater!!.inflate(R.layout.view_list_product,
                container,false)
        ButterKnife.bind(this, root)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        materialPresenter.view = this
        setupRecyclerView()
        async {
            materialPresenter.executeGetMaterial()
        }
    }

    private fun setupRecyclerView(){
        rvProducts!!.setHasFixedSize(true)
        rvProducts!!.layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ChangeFormat.addDecorationRecycler(rvProducts!!, context)
        }
    }

    override fun showMessage(message: String) {
        context.toast(message)
    }

    override fun showError(message: String) {
        context.toast(message)
    }

    override fun <T> executeTask(obj: T) {
        if (obj != null && obj is RealmResults<*>){
            val list = obj.filterIsInstance<Material>()
            this.adapter = ItemProductRealmAdapter(list as OrderedRealmCollection<Material>,
                    true){
                val materialModel = MaterialModel()
                materialModel.code = it.code
                materialModel.detail = it.detail
                listProduct.add(materialModel)
            }
            rvProducts!!.adapter = this.adapter
        }
    }

}