package com.hiddenodds.trebol.presentation.presenter

import com.hiddenodds.trebol.domain.data.MapperCustomer
import com.hiddenodds.trebol.domain.interactor.DeleteCustomersUseCase
import com.hiddenodds.trebol.domain.interactor.GetDownloadUseCase
import com.hiddenodds.trebol.domain.interactor.SaveListCustomerUseCase
import com.hiddenodds.trebol.presentation.model.DownloadModel
import com.hiddenodds.trebol.tools.Variables
import io.reactivex.observers.DisposableObserver
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class SaveCustomerPresenter @Inject constructor(private val getDownloadUseCase:
                                                GetDownloadUseCase,
                                                private val saveListCustomerUseCase:
                                                  SaveListCustomerUseCase,
                                                private val deleteCustomersUseCase:
                                                  DeleteCustomersUseCase):
        BasePresenter(){

    private var listTechnicals: ArrayList<String> = ArrayList()
    private var codeTechnical: String = ""
    private var listMapperCustomer: ArrayList<MapperCustomer> = ArrayList()
    private var countTech = 0


    fun executeGetCustomer(){
        this.listTechnicals = ArrayList(Variables.listTechnicals)
        this.listTechnicals.add(Variables.codeTechMaster)
        this.countTech = this.listTechnicals.size
        getCustomersNext()
    }

    private fun executeGetDownload(code: String){
        getDownloadUseCase.code = code
        getDownloadUseCase.execute(DownloadObserver())

    }

    private fun buildListMapper(jsonArray: JSONArray){

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
    private fun buildNotification(jsonString: String){
        this.listMapperCustomer = ArrayList()
        if (jsonString.isNotEmpty()){
            val jsonArray = JSONArray(jsonString)
            buildListMapper(jsonArray)
        }

        deleteCustomers()

    }


    private fun saveListCustomer(){
        if (listMapperCustomer.size != 0){
            saveListCustomerUseCase
                    .listMapperCustomer = this.listMapperCustomer
            saveListCustomerUseCase.execute(SaveCustomerObserver())

        }else{
            this.countTech -= 1
            if (this.countTech == 0){
                stopProgress()
            }
            getCustomersNext()
        }
    }

    private fun stopProgress(){
        view!!.executeTask(4)
    }

    private fun getCustomersNext(){
        if (listTechnicals.isNotEmpty()){
            val code = listTechnicals.last()
            listTechnicals.remove(code)
            this.codeTechnical = code
            executeGetDownload(code)
        }
    }


    private fun deleteCustomers(){
        deleteCustomersUseCase.code = this.codeTechnical
        deleteCustomersUseCase.fieldName = "tech"
        deleteCustomersUseCase.execute(DeleteCustomerObserver())
    }

    override fun destroy() {
        super.destroy()
        getDownloadUseCase.dispose()
        saveListCustomerUseCase.dispose()
        deleteCustomersUseCase.dispose()
    }

    inner class SaveCustomerObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            getCustomersNext()
        }

        override fun onComplete() {
            countTech -= 1
            if (countTech == 0){
                stopProgress()
            }
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }

    inner class DownloadObserver: DisposableObserver<DownloadModel>(){
        override fun onNext(t: DownloadModel) {
            buildNotification(t.customer)
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