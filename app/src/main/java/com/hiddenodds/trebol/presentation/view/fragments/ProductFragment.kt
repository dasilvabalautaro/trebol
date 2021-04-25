package com.hiddenodds.trebol.presentation.view.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.presentation.components.ItemProductAdapter
import com.hiddenodds.trebol.presentation.interfaces.ILoadDataView
import com.hiddenodds.trebol.presentation.model.MaterialModel
import com.hiddenodds.trebol.tools.ChangeFormat
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class ProductFragment: NotificationFragment(), ILoadDataView {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_products)
    @JvmField var rvProducts: RecyclerView? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.sv_product)
    @JvmField var svProduct: SearchView? = null

    companion object Factory {
        private const val inputNotification = "notify_"
        private const val inputTechnical = "technical_"
        fun newInstance(arg1: String? = null, arg2: String? = null):
                ProductFragment = ProductFragment().apply{
            this.arguments = Bundle().apply {
                this.putString(inputNotification, arg1)
                this.putString(inputTechnical, arg2)
            }

        }
    }

    private val codeNotification: String? by lazy { this.requireArguments()
            .getString(inputNotification) }
    private val codeTechnical: String? by lazy { this.requireArguments()
            .getString(inputTechnical) }


    private var observableList: Subject<List<MaterialModel>> = PublishSubject.create()
    private var adapter: ItemProductAdapter? = null
    private var listMaterial: ArrayList<MaterialModel>? = null
    private var inputMethodManager: InputMethodManager ? = null

    init {
        val list = observableList.map { l -> l }
        disposable.add(list.observeOn(AndroidSchedulers.mainThread())
                .subscribe { l ->
                    kotlin.run {
                        if (l.isNotEmpty()){

                            removeFragmentProduct()
                            val frg = requireActivity().supportFragmentManager
                                    .findFragmentByTag(OrderFragment::class
                                            .java.simpleName)
                            requireActivity().supportFragmentManager
                                    .popBackStack(OrderFragment::class.java.simpleName,
                                            1)
                            requireActivity().supportFragmentManager
                                    .beginTransaction()
                                    .replace(R.id.flContent, frg!!)
                                    .commit()

                            onDestroy()
                        }
                    }
                })
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val root: View = inflater.inflate(R.layout.view_list_product,
                container,false)
        ButterKnife.bind(this, root)
        return root
    }

    override fun onResume() {
        super.onResume()

        codeTechnical?.let { ChangeFormat.deleteCacheTechnical(it) }
        Thread.sleep(1000)
    }

    override fun onStart() {
        super.onStart()
        materialPresenter.view = this
        setupRecyclerView()
        svProduct!!.queryHint = resources.getString(R.string.lbl_hint_search)
        GlobalScope.async {
            materialPresenter.executeGetMaterial()
        }
        inputMethodManager = requireContext()
                .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        svProduct!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return if (newText.length > 2){
                    GlobalScope.async {
                        val queryList = listMaterial!!
                                .filter{ it.code.contains(newText
                                        .toUpperCase())} as ArrayList
                        if (queryList.isNotEmpty()){
                            adapter!!.setObjectList(queryList)
                            adapter!!.notifyDataSetChanged()
                            activity!!.runOnUiThread {
                                rvProducts!!.refreshDrawableState()
                                rvProducts!!.scrollToPosition(0)

                            }
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
        rvProducts!!.layoutManager = LinearLayoutManager(requireActivity(),
                LinearLayoutManager.VERTICAL, false)
        ChangeFormat.addDecorationRecycler(rvProducts!!, requireContext())
        adapter = ItemProductAdapter{
            val materialModel = MaterialModel()
            materialModel.code = it.code
            materialModel.detail = it.detail
            materialModel.id = it.id
            listMaterialSelect.add(materialModel)
            observableList.onNext(listMaterialSelect)
        }

        rvProducts!!.adapter = adapter

    }

    override fun showMessage(message: String) {
        requireContext().toast(message)
    }

    override fun showError(message: String) {
        requireContext().toast(message)
    }

    override fun <T> executeTask(obj: T) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T> executeTask(objList: List<T>) {
        if (objList.isNotEmpty()){
            GlobalScope.async {
                listMaterial = ArrayList(objList.filterIsInstance<MaterialModel>() as ArrayList)
                adapter!!.setObjectList(listMaterial!!)
                requireActivity().runOnUiThread {
                    rvProducts!!.scrollToPosition(0)
                }

            }

        }else{
            requireContext().toast(requireContext().getString(R.string.list_not_found))
        }

    }

}