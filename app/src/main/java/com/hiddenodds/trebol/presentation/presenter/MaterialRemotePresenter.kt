package com.hiddenodds.trebol.presentation.presenter

import com.hiddenodds.trebol.App
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.domain.data.MapperMaterial
import com.hiddenodds.trebol.domain.interactor.DeleteListMaterialUseCase
import com.hiddenodds.trebol.domain.interactor.GetRemoteDataUseCase
import com.hiddenodds.trebol.domain.interactor.SaveListMaterialUseCase
import com.hiddenodds.trebol.model.persistent.network.StatementSQL
import io.reactivex.observers.DisposableObserver
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject


class MaterialRemotePresenter @Inject constructor(private val getRemoteDataUseCase:
                                                  GetRemoteDataUseCase,
                                                  private val saveListMaterialUseCase:
                                                  SaveListMaterialUseCase,
                                                  private val deleteListMaterialUseCase:
                                                  DeleteListMaterialUseCase):
        BasePresenter(){
    private val listMapperMaterial: ArrayList<MapperMaterial> = ArrayList()

    fun executeQueryRemote(){
        if ((context as App).connectionNetwork.isOnline()){
            getRemoteDataUseCase.sql = StatementSQL.getItems()
            getRemoteDataUseCase.execute(ListObserver())

        }else{
            showError(context.resources.getString(R.string.network_not_found))
        }
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
        deleteListMaterial()
    }

    private fun saveListMaterial(){
        if (listMapperMaterial.size != 0){
            saveListMaterialUseCase
                    .listMapperMaterial = this.listMapperMaterial
            saveListMaterialUseCase.execute(SaveMaterialObserver())

        }else{
            showError(context.resources.getString(R.string.list_not_found))
        }
    }

    private fun deleteListMaterial(){
        if (listMapperMaterial.size != 0){
            deleteListMaterialUseCase.execute(DeleteMaterialObserver())

        }else{
            showError(context.resources.getString(R.string.list_not_found))
        }
    }
    private fun stopProgress(){
        view!!.executeTask(8)
    }

    override fun destroy() {
        super.destroy()
        getRemoteDataUseCase.dispose()
        saveListMaterialUseCase.dispose()
        deleteListMaterialUseCase.dispose()
    }

    inner class ListObserver: DisposableObserver<JSONArray>(){
        override fun onNext(t: JSONArray) {
            buildObjets(t)
        }

        override fun onComplete() {
            //showMessage(context.resources.getString(R.string.download_complete))
            stopProgress()
            getRemoteDataUseCase.dispose()

        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }

    inner class DeleteMaterialObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            saveListMaterial()
        }

        override fun onComplete() {
            println(context.resources.getString(R.string.delete_complete))
            deleteListMaterialUseCase.dispose()
            //showMessage(context.resources.getString(R.string.delete_complete))
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                println(e.message!!)
                //showError(e.message!!)
            }
        }
    }

    inner class SaveMaterialObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            println(context.resources.getString(R.string.material_save))
            //showMessage(context.resources.getString(R.string.material_save))

        }

        override fun onComplete() {
            println(context.resources.getString(R.string.task_complete))
            //showMessage(context.resources.getString(R.string.task_complete))
            saveListMaterialUseCase.dispose()
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                println(e.message!!)
                //showError(e.message!!)
            }
        }
    }
}