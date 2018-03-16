package com.hiddenodds.trebolv2.presentation.presenter

import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.domain.data.MapperCustomer
import com.hiddenodds.trebolv2.domain.interactor.DeleteCustomersUseCase
import com.hiddenodds.trebolv2.domain.interactor.GetRemoteDataUseCase
import com.hiddenodds.trebolv2.domain.interactor.GetTechnicalMasterUseCase
import com.hiddenodds.trebolv2.domain.interactor.SaveListCustomerUseCase
import com.hiddenodds.trebolv2.model.persistent.network.StatementSQL
import com.hiddenodds.trebolv2.presentation.model.TechnicalModel
import com.hiddenodds.trebolv2.tools.Constants
import com.hiddenodds.trebolv2.tools.PreferenceHelper
import com.hiddenodds.trebolv2.tools.PreferenceHelper.get
import io.reactivex.observers.DisposableObserver
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class CustomerRemotePresenter @Inject constructor(private val getRemoteDataUseCase:
                                                  GetRemoteDataUseCase,
                                                  private val getTechnicalMasterUseCase:
                                                  GetTechnicalMasterUseCase,
                                                  private val saveListCustomerUseCase:
                                                  SaveListCustomerUseCase,
                                                  private val deleteCustomersUseCase:
                                                  DeleteCustomersUseCase):
        BasePresenter(){

    private var listTechnicals: ArrayList<String> = ArrayList()
    private var codeTechnical: String = ""
    private var listMapperCustomer: ArrayList<MapperCustomer> = ArrayList()



    fun executeGetTechnicalMaster(){
        val prefs = PreferenceHelper.customPrefs(context,
                Constants.PREFERENCE_TREBOL)
        val code: String? = prefs[Constants.TECHNICAL_KEY, ""]
        val password: String? = prefs[Constants.TECHNICAL_PASSWORD, ""]
        if (!code.isNullOrEmpty() && !password.isNullOrEmpty()){
            getTechnicalMasterUseCase.code = code!!
            getTechnicalMasterUseCase.password = password!!
            getTechnicalMasterUseCase.execute(TechObserver())
        }else{
            println("Technical not found")
        }
    }

    private fun executeGetRemote(code: String){
        if ((context as App).connectionNetwork.isOnline()) {
            this.codeTechnical = code
            getRemoteDataUseCase.sql = StatementSQL.getCustomer(code)
            getRemoteDataUseCase.execute(ListObserver())
        }else{
            showError(context.resources.getString(R.string.network_not_found))
        }
    }

    private fun buildListMapper(jsonArray: JSONArray){
        listMapperCustomer = ArrayList()

        if (jsonArray.length() != 0){
            (0 until jsonArray.length()).forEach { i ->
                val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                val mapperCustomer = MapperCustomer()
                
                if (jsonObject.has("CODIGOAVISO")){
                    mapperCustomer.code = jsonObject.getString("CODIGOAVISO")?: ""
                    mapperCustomer.code = mapperCustomer.code.trim()
                }

                if (jsonObject.has("TECNICO")){
                    mapperCustomer.tech = jsonObject.getString("TECNICO")?: ""
                    mapperCustomer.tech = mapperCustomer.tech.trim()
                }
                if (jsonObject.has("PERSONA")){
                    mapperCustomer.name = jsonObject.getString("PERSONA")?: ""
                    mapperCustomer.name = mapperCustomer.name.trim()
                }

                if (jsonObject.has("TELEFONO1")){
                    mapperCustomer.phone = jsonObject.getString("TELEFONO1")?: ""
                    mapperCustomer.phone = mapperCustomer.phone.trim()
                }

                if (jsonObject.has("EMAIL")){
                    mapperCustomer.email = jsonObject.getString("EMAIL")?: ""
                    mapperCustomer.email = mapperCustomer.email.trim()
                }

                listMapperCustomer.add(mapperCustomer)
            }

        }
    }

    private fun buildObjets(jsonArray: JSONArray){
        buildListMapper(jsonArray)

        deleteCustomers()

    }

    private fun saveListCustomer(){
        if (listMapperCustomer.size != 0){
            saveListCustomerUseCase
                    .listMapperCustomer = this.listMapperCustomer
            saveListCustomerUseCase.execute(SaveCustomerObserver())

        }else{
            getCustomersNext()
        }
    }

    private fun stopProgress(){
        view!!.executeTask(2)
    }

    private fun getCustomersNext(){
        if (listTechnicals.isNotEmpty()){
            val code = listTechnicals.last()
            listTechnicals.remove(code)
            executeGetRemote(code)
        }else{
            stopProgress()
            showMessage(context.resources.getString(R.string.customer_download))
        }
    }


    private fun deleteCustomers(){
        deleteCustomersUseCase.code = this.codeTechnical
        deleteCustomersUseCase.fieldName = "tech"
        deleteCustomersUseCase.execute(DeleteCustomerObserver())
    }


    inner class SaveCustomerObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            getCustomersNext()
        }

        override fun onComplete() {
            //showMessage(context.resources.getString(R.string.save_data))
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }

    inner class TechObserver: DisposableObserver<TechnicalModel>(){
        override fun onNext(t: TechnicalModel) {
            listTechnicals = ArrayList(t.trd)
            executeGetRemote(t.code)
        }

        override fun onComplete() {
            //showMessage(context.resources.getString(R.string.get_complete))
        }

        override fun onError(e: Throwable) {
            println("Error: Technical master")
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }

    inner class ListObserver: DisposableObserver<JSONArray>(){
        override fun onNext(t: JSONArray) {
            buildObjets(t)
        }

        override fun onComplete() {
            //showMessage(context.resources.getString(R.string.download_complete))
        }

        override fun onError(e: Throwable) {
            println("Error: JSONARRAY")
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }

    inner class DeleteCustomerObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            saveListCustomer()
        }

        override fun onComplete() {
            //showMessage(context.resources.getString(R.string.delete_complete))
        }

        override fun onError(e: Throwable) {
            println("Error: Delete customer")
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }

}