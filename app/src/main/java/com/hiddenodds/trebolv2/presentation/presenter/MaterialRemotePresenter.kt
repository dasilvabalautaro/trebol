package com.hiddenodds.trebolv2.presentation.presenter

import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.domain.data.MapperMaterial
import com.hiddenodds.trebolv2.domain.interactor.GetRemoteDataUseCase
import com.hiddenodds.trebolv2.domain.interactor.SaveListMaterialUseCase
import com.hiddenodds.trebolv2.model.persistent.network.StatementSQL
import io.reactivex.observers.DisposableObserver
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject


class MaterialRemotePresenter @Inject constructor(private val getRemoteDataUseCase:
                                                  GetRemoteDataUseCase,
                                                  private val saveListMaterialUseCase:
                                                  SaveListMaterialUseCase):
        BasePresenter(){
    private val listMapperMaterial: ArrayList<MapperMaterial> = ArrayList()

    init {
        this.iHearMessage = getRemoteDataUseCase
    }

    fun executeQueryRemote(){
        getRemoteDataUseCase.sql = StatementSQL.getItems()
        getRemoteDataUseCase.execute(ListObserver())
    }

    private fun buildListMapper(jsonArray: JSONArray){
        if (jsonArray.length() != 0){
            (0 until jsonArray.length()).forEach { i ->
                val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                val mapperMaterial = MapperMaterial()

                if (jsonObject.has("CODIGO")){
                    mapperMaterial.code = jsonObject.getString("CODIGO")?: ""
                    mapperMaterial.code = mapperMaterial.code.trim()
                }
                if (jsonObject.has("DESCRIPCIO")){
                    mapperMaterial.detail = jsonObject.getString("DESCRIPCIO")?: ""
                    mapperMaterial.detail = mapperMaterial.detail.trim()
                }

                listMapperMaterial.add(mapperMaterial)
            }

        }
    }

    private fun buildObjets(jsonArray: JSONArray){
        buildListMapper(jsonArray)
        saveListMaterial()
    }

    private fun saveListMaterial(){
        if (listMapperMaterial.size != 0){
            this.iHearMessage = saveListMaterialUseCase
            saveListMaterialUseCase
                    .listMapperMaterial = this.listMapperMaterial
            saveListMaterialUseCase.execute(SaveMaterialObserver())

        }else{
            showError(context.resources.getString(R.string.list_not_found))
        }
    }

    inner class ListObserver: DisposableObserver<JSONArray>(){
        override fun onNext(t: JSONArray) {
            buildObjets(t)
        }

        override fun onComplete() {
            showMessage(context.resources.getString(R.string.task_complete))
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }

    inner class SaveMaterialObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            showMessage(context.resources.getString(R.string.material_save))

        }

        override fun onComplete() {
            showMessage(context.resources.getString(R.string.task_complete))
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }
}