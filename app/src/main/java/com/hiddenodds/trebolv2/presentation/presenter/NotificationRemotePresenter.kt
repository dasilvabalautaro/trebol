package com.hiddenodds.trebolv2.presentation.presenter

import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.domain.data.MapperNotification
import com.hiddenodds.trebolv2.domain.interactor.GetRemoteDataUseCase
import com.hiddenodds.trebolv2.domain.interactor.SaveListNotificationUseCase
import com.hiddenodds.trebolv2.model.persistent.caching.CachingLruRepository
import com.hiddenodds.trebolv2.model.persistent.network.StatementSQL
import com.hiddenodds.trebolv2.presentation.model.TechnicalModel
import com.hiddenodds.trebolv2.tools.Constants
import io.reactivex.observers.DisposableObserver
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject


class NotificationRemotePresenter @Inject constructor(private val getRemoteDataUseCase:
                                                      GetRemoteDataUseCase,
                                                      private val saveListNotificationUseCase:
                                                      SaveListNotificationUseCase):
        BasePresenter(){
    private val listMapperNotification: ArrayList<MapperNotification> = ArrayList()

    init {
        this.iHearMessage = getRemoteDataUseCase
    }

    fun executeQueryRemote(){
        if ((context as App).connectionNetwork.isOnline()){
            val tech= CachingLruRepository
                    .instance
                    .getLru()
                    .get(Constants.CACHE_TECHNICAL_MASTER) as TechnicalModel
            var code = tech.code

            getRemoteDataUseCase.sql = StatementSQL.getNotifications(code)
            getRemoteDataUseCase.execute(ListObserver())

        }else{
            showError(context.resources.getString(R.string.network_not_found))
        }
    }

    private fun buildListMapper(jsonArray: JSONArray){
        if (jsonArray.length() != 0){
            (0 until jsonArray.length()).forEach { i ->
                val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                val mapperNotification = MapperNotification()

                if (jsonObject.has("CODIGOMAQUINA")){
                    mapperNotification.machine = jsonObject.getString("CODIGOMAQUINA")?: ""
                    mapperNotification.machine = mapperNotification.machine.trim()
                }
                if (jsonObject.has("CODIGOAVISO")){
                    mapperNotification.code = jsonObject.getString("CODIGOAVISO")?: ""
                    mapperNotification.code = mapperNotification.code.trim()
                }


                if (jsonObject.has("FECHAINICIO")){
                    mapperNotification.dateInit = jsonObject.getString("FECHAINICIO")?: ""
                    mapperNotification.dateInit = mapperNotification.dateInit.trim()
                }
                if (jsonObject.has("TRB_TIPO")){
                    mapperNotification.type = jsonObject.getString("TRB_TIPO")?: ""
                    mapperNotification.type = mapperNotification.type.trim()
                }


                if (jsonObject.has("TRB_RAZONSOCIAL")){
                    mapperNotification.businessName = jsonObject.getString("TRB_RAZONSOCIAL")?: ""
                    mapperNotification.businessName = mapperNotification.businessName.trim()
                }
                if (jsonObject.has("FECHAREALIZACIO")){
                    mapperNotification.dateCompleted = jsonObject.getString("FECHAREALIZACIO")?: ""
                    mapperNotification.dateCompleted = mapperNotification.dateCompleted.trim()
                }


                if (jsonObject.has("PROVINCIA")){
                    mapperNotification.province = jsonObject.getString("PROVINCIA")?: ""
                    mapperNotification.province = mapperNotification.province.trim()
                }
                if (jsonObject.has("LOCALIDAD")){
                    mapperNotification.locality = jsonObject.getString("LOCALIDAD")?: ""
                    mapperNotification.locality = mapperNotification.locality.trim()
                }


                if (jsonObject.has("TRB_SINTOMA")){
                    mapperNotification.symptom = jsonObject.getString("TRB_SINTOMA")?: ""
                    mapperNotification.symptom = mapperNotification.symptom.trim()
                }
                if (jsonObject.has("DIRECCION")){
                    mapperNotification.address = jsonObject.getString("DIRECCION")?: ""
                    mapperNotification.address = mapperNotification.address.trim()
                }


                if (jsonObject.has("NSERIE")){
                    mapperNotification.series = jsonObject.getString("NSERIE")?: ""
                    mapperNotification.series = mapperNotification.series.trim()
                }
                if (jsonObject.has("TECNICO")){
                    mapperNotification.idTech = jsonObject.getString("TECNICO")?: ""
                    mapperNotification.idTech = mapperNotification.idTech.trim()
                }


                if (jsonObject.has("CODIGOPRODUCTO")){
                    mapperNotification.product = jsonObject.getString("CODIGOPRODUCTO")?: ""
                    mapperNotification.product = mapperNotification.product.trim()
                }
                if (jsonObject.has("TRB_TINTA")){
                    mapperNotification.ink = jsonObject.getString("TRB_TINTA")?: ""
                    mapperNotification.ink = mapperNotification.ink.trim()
                }


                if (jsonObject.has("TRB_PEAJEPRE")){
                    mapperNotification.peaje = jsonObject.getString("TRB_PEAJEPRE")?: ""
                    mapperNotification.peaje = mapperNotification.peaje.trim()
                }
                if (jsonObject.has("TRB_SATD")){
                    mapperNotification.satd = jsonObject.getString("TRB_SATD")?: ""
                    mapperNotification.satd = mapperNotification.satd.trim()
                }
                if (jsonObject.has("TRB_SATDK")){
                    mapperNotification.satdk = jsonObject.getString("TRB_SATDK")?: ""
                    mapperNotification.satdk = mapperNotification.satdk.trim()
                }
                if (jsonObject.has("COMERCIAL")){
                    mapperNotification.trade = jsonObject.getString("COMERCIAL")?: ""
                    mapperNotification.trade = mapperNotification.trade.trim()
                }

                listMapperNotification.add(mapperNotification)
            }

        }
    }
    private fun buildObjets(jsonArray: JSONArray){
        buildListMapper(jsonArray)
        saveListNotification()
    }
    private fun saveListNotification(){
        if (listMapperNotification.size != 0){
            this.iHearMessage = saveListNotificationUseCase
            saveListNotificationUseCase
                    .listMapperNotification = this.listMapperNotification
            saveListNotificationUseCase.execute(SaveNotificationObserver())

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

    inner class SaveNotificationObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            showMessage(context.resources.getString(R.string.notification_save))
            //stopProgress()
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