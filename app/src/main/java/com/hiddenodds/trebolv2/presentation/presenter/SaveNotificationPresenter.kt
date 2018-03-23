package com.hiddenodds.trebolv2.presentation.presenter

import android.os.Handler
import android.os.Looper
import com.hiddenodds.trebolv2.domain.data.MapperNotification
import com.hiddenodds.trebolv2.domain.interactor.DeleteNotificationsOfTechnicalUseCase
import com.hiddenodds.trebolv2.domain.interactor.GetDownloadUseCase
import com.hiddenodds.trebolv2.domain.interactor.GetLisTypeNotificationUseCase
import com.hiddenodds.trebolv2.domain.interactor.SaveListNotificationUseCase
import com.hiddenodds.trebolv2.presentation.model.DownloadModel
import com.hiddenodds.trebolv2.presentation.model.TypeNotificationModel
import com.hiddenodds.trebolv2.tools.ChangeFormat
import com.hiddenodds.trebolv2.tools.Variables
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject


class SaveNotificationPresenter @Inject constructor(private val getDownloadUseCase:
                                                    GetDownloadUseCase,
                                                    private val saveListNotificationUseCase:
                                                      SaveListNotificationUseCase,
                                                    private val deleteNotificationsOfTechnicalUseCase:
                                                      DeleteNotificationsOfTechnicalUseCase,
                                                    private val getLisTypeNotificationUseCase:
                                                      GetLisTypeNotificationUseCase):
        BasePresenter(){
    private var listMapperNotification: ArrayList<MapperNotification> = ArrayList()
    private var listTypeNotification: List<TypeNotificationModel> = ArrayList()
    private var listTechnicals: ArrayList<String> = ArrayList()
    private var codeTechnical: String = ""
    private var countTech = 0
    private val handler = Handler(Looper.getMainLooper())

    fun executeSaveNotification(){
        this.listTechnicals = ArrayList(Variables.listTechnicals)
        this.listTechnicals.add(Variables.codeTechMaster)
        this.countTech = this.listTechnicals.size
        getLisTypeNotificationUseCase.execute(ListTypeNotificationObserver())
    }

    private fun executeGetDowload(code: String){
        getDownloadUseCase.code = code
        getDownloadUseCase.execute(DownloadObserver())
        
    }

    private fun buildListMapper(jsonArray: JSONArray){
        //println("Tamaño Array: " + jsonArray.length().toString())
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
                    mapperNotification.dateInit = ChangeFormat.convertDate(mapperNotification.dateInit.trim())
                }
                if (jsonObject.has("TRB_TIPO")){
                    var dataField = jsonObject.getString("TRB_TIPO")?: ""
                    dataField = dataField.trim()

                    if (listTypeNotification.isNotEmpty()){

                        val found = listTypeNotification.find { it.code == dataField}
                        if (found != null){
                            mapperNotification.type = found.description
                        }

                    }else{
                        mapperNotification.type = dataField
                    }

                }


                if (jsonObject.has("TRB_RAZONSOCIAL")){
                    mapperNotification.businessName = jsonObject.getString("TRB_RAZONSOCIAL")?: ""
                    mapperNotification.businessName = mapperNotification.businessName.trim()
                }
                if (jsonObject.has("FECHAREALIZACIO")){
                    mapperNotification.dateCompleted = jsonObject.getString("FECHAREALIZACIO")?: ""
                    mapperNotification.dateCompleted = ChangeFormat.convertDate(mapperNotification.dateCompleted.trim())
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
    private fun buildNotification(jsonString: String){
        deleteListNotification()

        if (jsonString.isNotEmpty()){
            async(CommonPool) {
                val jsonArray = JSONArray(jsonString)
                buildListMapper(jsonArray)
                saveListNotification()
            }
        }else{
            this.countTech -= 1
            if (this.countTech == 0){
                stopProgress()
            }
            getNotificationsNextTechnical()
        }

    }

    private fun deleteListNotification(){
        deleteNotificationsOfTechnicalUseCase.code = this.codeTechnical
        deleteNotificationsOfTechnicalUseCase.execute(DeleteNotificationObserver())
    }

    private fun saveListNotification(){
        if (this.listMapperNotification.size != 0){
            saveListNotificationUseCase
                    .listMapperNotification = ArrayList(this.listMapperNotification)
            /*println("listas cargadas:" + this.codeTechnical +
                    "  cantidad:" + this.listMapperNotification.size.toString())*/
            this.listMapperNotification = ArrayList()
            saveListNotificationUseCase.execute(SaveNotificationObserver())

        }
    }

    private fun stopProgress(){
        handler.post { view!!.executeTask(3)}
    }

    private fun getNotificationsNextTechnical(){
        if (this.listTechnicals.size != 0){
            val code = this.listTechnicals.last()
            this.listTechnicals.remove(code)
            this.codeTechnical = code
            executeGetDowload(code)
        }
    }

    override fun destroy() {
        super.destroy()
        getDownloadUseCase.dispose()
        saveListNotificationUseCase.dispose()
        deleteNotificationsOfTechnicalUseCase.dispose()
        getLisTypeNotificationUseCase.dispose()
    }
    inner class ListTypeNotificationObserver:
            DisposableObserver<List<TypeNotificationModel>>(){
        override fun onNext(t: List<TypeNotificationModel>) {
            listTypeNotification = t
            getNotificationsNextTechnical()
        }

        override fun onComplete() {
            //showMessage(context.resources.getString(R.string.get_complete))
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }

    inner class DownloadObserver: DisposableObserver<DownloadModel>(){
        override fun onNext(t: DownloadModel) {
            buildNotification(t.notification)
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

    inner class DeleteNotificationObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            //saveListNotification()
        }

        override fun onComplete() {
            //showMessage(context.resources.getString(R.string.delete_complete))
        }

        override fun onError(e: Throwable) {
            println("Error: Delete Notification")
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }

    inner class SaveNotificationObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            getNotificationsNextTechnical()
        }

        override fun onComplete() {
            countTech -= 1
            if (countTech == 0){
                stopProgress()
            }

        }

        override fun onError(e: Throwable) {
            println("Error: Save Notification")
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }

}