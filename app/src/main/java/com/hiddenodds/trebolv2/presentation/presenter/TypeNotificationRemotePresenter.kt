package com.hiddenodds.trebolv2.presentation.presenter

import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.domain.data.MapperTypeNotification
import com.hiddenodds.trebolv2.domain.interactor.GetRemoteDataUseCase
import com.hiddenodds.trebolv2.domain.interactor.SaveListTypeNotificationUseCase
import com.hiddenodds.trebolv2.model.persistent.network.StatementSQL
import io.reactivex.observers.DisposableObserver
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject


class TypeNotificationRemotePresenter @Inject constructor(private val getRemoteDataUseCase:
                                                          GetRemoteDataUseCase,
                                                          private val saveListTypeNotificationUseCase:
                                                          SaveListTypeNotificationUseCase):
        BasePresenter() {

    private val listMapperTypeNotification:
            ArrayList<MapperTypeNotification> = ArrayList()

    init {
        this.iHearMessage = getRemoteDataUseCase
    }

    fun executeQueryRemote(){
        if ((context as App).connectionNetwork.isOnline()){
            getRemoteDataUseCase.sql = StatementSQL.getTypeNotification()
            getRemoteDataUseCase.execute(ListObserver())

        }else{
            showError(context.resources.getString(R.string.network_not_found))
        }
    }

    private fun buildListMapper(jsonArray: JSONArray){
        if (jsonArray.length() != 0){
            (0 until jsonArray.length()).forEach { i ->
                val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                val mapperTypeNotification = MapperTypeNotification()

                if (jsonObject.has("TRB_TIPO")){
                    var dataField = jsonObject.getString("TRB_TIPO")?: ""
                    dataField = dataField.trim()
                    if (dataField.isNotEmpty()){
                        dataField = dataField.toInt().toString()
                    }
                    mapperTypeNotification.code = dataField
                }
                if (jsonObject.has("DESCRIPCION")){
                    mapperTypeNotification.description = jsonObject.getString("DESCRIPCION")?: ""
                    mapperTypeNotification.description = mapperTypeNotification.description.trim()
                }

                listMapperTypeNotification.add(mapperTypeNotification)
            }

        }
    }

    private fun saveListTypeNotification(){
        if (listMapperTypeNotification.size != 0){
            this.iHearMessage = saveListTypeNotificationUseCase
            saveListTypeNotificationUseCase
                    .listMapperTypeNotification = this.listMapperTypeNotification
            saveListTypeNotificationUseCase.execute(SaveTypeNotificationObserver())

        }else{
            showError(context.resources.getString(R.string.list_not_found))
        }
    }

    private fun buildObjets(jsonArray: JSONArray){
        buildListMapper(jsonArray)
        saveListTypeNotification()
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

    inner class SaveTypeNotificationObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            showMessage(context.resources.getString(R.string.type_notification_save))

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