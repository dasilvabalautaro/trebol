package com.hiddenodds.trebolv2.presentation.presenter

import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.domain.data.MapperNotification
import com.hiddenodds.trebolv2.domain.interactor.*
import com.hiddenodds.trebolv2.model.persistent.network.StatementSQL
import com.hiddenodds.trebolv2.presentation.model.TechnicalModel
import com.hiddenodds.trebolv2.presentation.model.TypeNotificationModel
import com.hiddenodds.trebolv2.tools.ChangeFormat
import com.hiddenodds.trebolv2.tools.Constants
import com.hiddenodds.trebolv2.tools.PreferenceHelper
import com.hiddenodds.trebolv2.tools.PreferenceHelper.get
import io.reactivex.observers.DisposableObserver
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject


class NotificationRemotePresenter @Inject constructor(private val getRemoteDataUseCase:
                                                      GetRemoteDataUseCase,
                                                      private val saveListNotificationUseCase:
                                                      SaveListNotificationUseCase,
                                                      private val getTechnicalMasterUseCase:
                                                      GetTechnicalMasterUseCase,
                                                      private val deleteNotificationsOfTechnicalUseCase:
                                                      DeleteNotificationsOfTechnicalUseCase,
                                                      private val addNotificationToTechnicalUseCase:
                                                      AddNotificationToTechnicalUseCase,
                                                      private val getLisTypeNotificationUseCase:
                                                      GetLisTypeNotificationUseCase):
        BasePresenter(){
    private var listMapperNotification: ArrayList<MapperNotification> = ArrayList()
    private var listTypeNotification: List<TypeNotificationModel> = ArrayList()
    private var listTechnicals: ArrayList<String> = ArrayList()
    private var codeTechnical: String = ""
    private var codeMaster: String = ""
    private var flag = false

    init {
        this.iHearMessage = getRemoteDataUseCase

    }

    fun executeDownloadNotification(){
        getLisTypeNotificationUseCase.execute(ListTypeNotificationObserver())
    }

    fun executeQueryRemote(){
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
            this.iHearMessage = getRemoteDataUseCase
            if ((this.codeMaster != this.codeTechnical) && !flag){
                this.flag = true
                stopProgress()
            }
            getRemoteDataUseCase.sql = StatementSQL.getNotifications(code)
            getRemoteDataUseCase.execute(ListObserver())
        }else{
            showError(context.resources.getString(R.string.network_not_found))
        }
    }

    private fun buildListMapper(jsonArray: JSONArray){
        listMapperNotification = ArrayList()

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
    private fun buildObjets(jsonArray: JSONArray){
        buildListMapper(jsonArray)
        deleteListNotification()

    }

    private fun deleteListNotification(){
        this.iHearMessage = deleteNotificationsOfTechnicalUseCase
        deleteNotificationsOfTechnicalUseCase.code = this.codeTechnical
        deleteNotificationsOfTechnicalUseCase.execute(DeleteNotificationObserver())
    }

    private fun addNotificationsTechnical(){
        this.iHearMessage = addNotificationToTechnicalUseCase
        addNotificationToTechnicalUseCase.codeTech = this.codeTechnical
        addNotificationToTechnicalUseCase.execute(AddNotificationTechnicalObserver())
    }

    private fun saveListNotification(){
        if (listMapperNotification.size != 0){
            this.iHearMessage = saveListNotificationUseCase
            saveListNotificationUseCase
                    .listMapperNotification = this.listMapperNotification
            saveListNotificationUseCase.execute(SaveNotificationObserver())

        }else{
            getNotificationsNextTechnical()
        }
    }

    private fun stopProgress(){
        view!!.executeTask(null)
    }

    private fun getNotificationsNextTechnical(){
        if (listTechnicals.isNotEmpty()){
            val code = listTechnicals.last()
            listTechnicals.remove(code)
            executeGetRemote(code)
        }else{

            showMessage(context.resources.getString(R.string.notification_save))
        }
    }

    inner class ListTypeNotificationObserver:
            DisposableObserver<List<TypeNotificationModel>>(){
        override fun onNext(t: List<TypeNotificationModel>) {
            listTypeNotification = t
            executeQueryRemote()
        }

        override fun onComplete() {
            showMessage(context.resources.getString(R.string.get_complete))
        }

        override fun onError(e: Throwable) {
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
            showMessage(context.resources.getString(R.string.download_complete))
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }

    inner class DeleteNotificationObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            saveListNotification()
        }

        override fun onComplete() {
            showMessage(context.resources.getString(R.string.delete_complete))
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }

    inner class SaveNotificationObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            addNotificationsTechnical()

        }

        override fun onComplete() {
            showMessage(context.resources.getString(R.string.save_data))
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }

    inner class AddNotificationTechnicalObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            getNotificationsNextTechnical()

        }

        override fun onComplete() {
            showMessage(context.resources.getString(R.string.add_notifications))
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }

    inner class TechObserver: DisposableObserver<TechnicalModel>(){
        override fun onNext(t: TechnicalModel) {
            listTechnicals = t.trd
            codeMaster = t.code
            executeGetRemote(t.code)
        }

        override fun onComplete() {
            showMessage(context.resources.getString(R.string.get_complete))
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }
}