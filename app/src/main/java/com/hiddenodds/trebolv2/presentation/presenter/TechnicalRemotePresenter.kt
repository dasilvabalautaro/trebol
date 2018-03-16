package com.hiddenodds.trebolv2.presentation.presenter

import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.domain.data.MapperTechnical
import com.hiddenodds.trebolv2.domain.interactor.GetRemoteDataUseCase
import com.hiddenodds.trebolv2.domain.interactor.SaveDependentTechnicalUseCase
import com.hiddenodds.trebolv2.domain.interactor.SaveListTechnicalUseCase
import com.hiddenodds.trebolv2.model.persistent.network.StatementSQL
import com.hiddenodds.trebolv2.tools.Constants
import com.hiddenodds.trebolv2.tools.PreferenceHelper
import com.hiddenodds.trebolv2.tools.PreferenceHelper.set
import io.reactivex.observers.DisposableObserver
import org.json.JSONArray
import org.json.JSONObject
import java.util.LinkedHashMap
import javax.inject.Inject
import kotlin.collections.ArrayList


class TechnicalRemotePresenter @Inject constructor(private val getRemoteDataUseCase:
                                                   GetRemoteDataUseCase,
                                                   private val saveListTechnicalUseCase:
                                                   SaveListTechnicalUseCase,
                                                   private val saveDependentTechnicalUseCase:
                                                   SaveDependentTechnicalUseCase):
        BasePresenter(){

    private val listMapperTechnical: ArrayList<MapperTechnical> = ArrayList()
    private val dependentTechnicians: LinkedHashMap<String,
            ArrayList<String>> = LinkedHashMap()
    private var flag = true

    fun executeQueryRemote(){
        if ((context as App).connectionNetwork.isOnline()){
            getRemoteDataUseCase.sql = StatementSQL.getTechnical()
            getRemoteDataUseCase.execute(ListObserver())

        }else{
            showError(context.resources.getString(R.string.network_not_found))
        }
    }

    private fun buildDependentTechnicians(jsonArray: JSONArray){
        if (jsonArray.length() != 0){
            (0 until jsonArray.length()).forEach { i ->
                val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                var code: String = ""

                if (jsonObject.has("TECNICO")){
                    code = jsonObject.getString("TECNICO")?: ""
                    code = code.trim()
                }

                if (jsonObject.has("TRD")){
                    var trd = jsonObject.getString("TRD")?: ""
                    trd = trd.trim()
                    if (!trd.isEmpty() && trd != code){
                        if (!dependentTechnicians.containsKey(trd)){
                            val list: ArrayList<String> = ArrayList()
                            list.add(code)
                            dependentTechnicians[trd] = list
                        }else{
                            dependentTechnicians[trd]!!.add(code)
                        }
                    }
                }
            }
        }
    }

    private fun buildListMapper(jsonArray: JSONArray){
        if (jsonArray.length() != 0){
            (0 until jsonArray.length()).forEach { i ->
                val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                val mapperTechnical = MapperTechnical()

                if (jsonObject.has("TECNICO")){
                    mapperTechnical.code = jsonObject.getString("TECNICO")?: ""
                    mapperTechnical.code = mapperTechnical.code.trim()
                }
                if (jsonObject.has("NOMBRE")){
                    mapperTechnical.name = jsonObject.getString("NOMBRE")?: ""
                    mapperTechnical.name = mapperTechnical.name.trim()
                }
                if (jsonObject.has("PASSWORD")){
                    mapperTechnical.password = jsonObject.getString("PASSWORD")?: ""
                    mapperTechnical.password = mapperTechnical.password.trim()
                }
                if (jsonObject.has("EMAIL")){
                    mapperTechnical.email = jsonObject.getString("EMAIL")?: ""
                    mapperTechnical.email = mapperTechnical.email.trim()
                }
                listMapperTechnical.add(mapperTechnical)
            }

        }
    }

    private fun buildObjets(jsonArray: JSONArray){
        buildListMapper(jsonArray)
        buildDependentTechnicians(jsonArray)
        saveListTechnicals()

    }

    private fun saveListTechnicals(){
        if (listMapperTechnical.size != 0){
            saveListTechnicalUseCase
                    .listMapperTechnical = this.listMapperTechnical
            saveListTechnicalUseCase.execute(SaveTechniciansObserver())

        }else{
            showError(context.resources.getString(R.string.list_not_found))
        }
    }
    
    private fun saveDependentTechnicals(){
        if (dependentTechnicians.count() != 0){
            saveDependentTechnicalUseCase
                    .dependentTechnicians = this.dependentTechnicians
            saveDependentTechnicalUseCase.execute(SaveTechniciansObserver())
        }
    }

    override fun destroy() {
        super.destroy()
        this.getRemoteDataUseCase.dispose()
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

    inner class SaveTechniciansObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            if (flag){
                flag = false
                saveDependentTechnicals()
            }else{
                val prefs = PreferenceHelper.customPrefs(context,
                        Constants.PREFERENCE_TREBOL)
                prefs[Constants.TECHNICAL_DB] = true
                showMessage(context.resources.getString(R.string.technicals_save))
            }

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